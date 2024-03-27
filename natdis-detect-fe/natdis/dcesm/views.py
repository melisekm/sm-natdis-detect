import logging

from django.shortcuts import render, redirect
from django.views import View
from django.contrib import messages

from dcesm.connectors.keycloak_auth import keycloak_authenticate
from dcesm.mock_data import mock_prediction


class Homepage(View):
    def get(self, request, *args, **kwargs):
        return redirect('/predict')


class ViewWithAPIClient(View):

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        # self.api_client = APIClient()


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


class CreatePrediction(View):
    def get(self, request):
        return render(request, 'predict.html')

    def post(self, request):
        payload = request.POST
        text = payload['SocialMediaPost']
        if isinstance(text, str):
            text = text.strip()
        if not text:
            messages.error(request, 'Please enter a text to predict.')
            return render(request, 'predict.html')
        # prediction = self.api_client.predict(text)
        if 'Ida' in text:
            prediction = mock_prediction['informative']
        else:
            prediction = mock_prediction['non-informative']
        return render(request, 'predict.html', {'prediction': prediction, 'text': text, 'detailed': False})


class PredictionDetail(View):
    def get(self, request, prediction_id):
        # prediction = self.api_client.get_prediction(prediction_id)
        if prediction_id == 1:
            prediction = mock_prediction['informative']
        elif prediction_id == 2:
            prediction = mock_prediction['non-informative']
        else:
            return render(request, '404.html')
        return render(request, 'prediction_detail.html', {'prediction': prediction, 'detailed':True})


class Predictions(View):
    def get(self, request):
        # predictions = self.api_client.get_predictions()
        predictions = [mock_prediction]
        return render(request, 'predictions.html', {'predictions': predictions})


class CreateEntityAnnotation(View):
    def get(self, request):
        return render(request, 'create_entity_annotation.html')


