import json
import logging

from django.contrib import messages
from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.views import View

from dcesm.connectors.api_client import APIClient
from dcesm.connectors.keycloak_auth import keycloak_authenticate
from dcesm.mock_data import mock_prediction
from dcesm.schemas import PredictionResponse


class Homepage(View):
    def get(self, request, *args, **kwargs):
        return redirect('/predict')


class ViewWithAPIClient(View):

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.api_client = APIClient()

    def setup(self, request, *args, **kwargs):
        if 'token' in request.session:
            self.api_client.token = request.session['token']
            self.api_client.token_expiration = request.session['token_expiration']
            self.api_client.user = request.session['user']
        super().setup(request, *args, **kwargs)


class Login(View):
    def get(self, request):
        opts = {}
        if 'login_message' in request.session:
            opts['message'] = request.session.pop('login_message')
        if 'token' in request.session:
            return redirect('predict')
        return render(request, 'login.html', opts)

    def post(self, request, *args, **kwargs):
        payload = request.POST
        try:
            auth_data = keycloak_authenticate(payload['username'], payload['password'])
        except Exception as e:
            logging.warning(f"Failed to authorize to Keycloak API. Reason: {e}")
            request.session['login_message'] = "Something went wrong. Please contact developers and try again later."
            return redirect('login')
        if auth_data['status'] == 'success':
            request.session['token'] = auth_data['token']
            request.session['token_expiration'] = auth_data['token_expiration']
            request.session['user'] = auth_data['decoded_token']
            return redirect('predict')
        else:
            request.session['login_message'] = auth_data['message']
            return redirect('login')


class Logout(View):
    def get(self, request):
        del request.session['token']
        del request.session['token_expiration']
        del request.session['user']
        request.session.modified = True
        request.session['login_message'] = "You have been successfully logged out."
        return redirect('login')


class CreatePrediction(ViewWithAPIClient):
    def get(self, request):
        return render(request, 'predict.html')

    def post(self, request):
        text = request.POST['SocialMediaPost']
        if isinstance(text, str):
            text = text.strip()
        if not text:
            messages.error(request, 'Please enter a text to predict.')
            return render(request, 'predict.html')
        try:
            prediction = self.api_client.predict(text)
        except Exception as e:
            logging.exception(e)
            messages.error(request, 'Failed to make prediction. Please try again later.')
            return render(request, 'predict.html')

        return render(
            request, 'predict.html',
            {'prediction': transform_prediction(prediction), 'text': text, 'detailed': False}
        )


class PredictionDetail(View):
    def get(self, request, prediction_id):
        # prediction = self.api_client.get_prediction(prediction_id)
        if prediction_id == 1:
            prediction = mock_prediction['informative']
        elif prediction_id == 2:
            prediction = mock_prediction['non-informative']
        else:
            return render(request, '404.html')
        return render(request, 'prediction_detail.html', {'prediction': prediction, 'detailed': True})


def truncate_with_ellipsis(text: str, max_len: int) -> str:
    if len(text) > max_len:
        return text[:max_len - 3] + "..."
    return text


class Predictions(View):
    def get(self, request):
        # predictions = self.api_client.get_predictions()
        data = [mock_prediction['informative'], mock_prediction['non-informative']]
        res = []
        for prediction in data:
            res.append({
                'id': prediction['id'],
                'created_at': prediction['created_at'].replace('T', ' ')[:16],
                'text': truncate_with_ellipsis(prediction['text'], 75),
                'label': prediction['label'],
                'confidence': prediction['confidence'],
                'entities': sum(
                    len(entities) for entities in prediction['entities'].values()
                ),
                'links': len(prediction['links']),
            })
        headers = [
            'ID',
            'Created At',
            'Text',
            'Label',
            'Confidence',
            'Entities',
            'Links',
        ]
        return render(request, 'predictions.html', {'predictions': res, 'headers': headers})


class CreateEntityAnnotation(View):
    def get(self, request):
        return render(request, 'create_entity_annotation.html')


class RatePrediction(View):
    def post(self, request):
        payload = json.loads(request.body.decode('utf-8'))
        logging.info(payload)
        # self.api_client.rate_prediction(payload['prediction_id'], payload['rating'])
        return JsonResponse({'message': 'Prediction has been rated successfully.'})


def transform_prediction(prediction: PredictionResponse):
    logging.info(prediction.model_dump())
    return mock_prediction['informative'] if prediction.informative else mock_prediction['non-informative']
