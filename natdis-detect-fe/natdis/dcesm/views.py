import json
import logging

from django.contrib import messages
from django.http import JsonResponse
from django.shortcuts import render, redirect
from django.views import View

from dcesm.connectors.api_client import APIClient
from dcesm.connectors.keycloak_auth import keycloak_authenticate
from dcesm.schemas import PredictionResponse, Entity, Link


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
            return render(request, 'predict.html', {'text': text})

        return render(
            request, 'predict.html',
            {'prediction': transform_prediction(prediction), 'text': text, 'detailed': False}
        )


class PredictionDetail(ViewWithAPIClient):
    def get(self, request, prediction_id):
        try:
            prediction = self.api_client.get_prediction(prediction_id)
        except Exception as e:
            logging.exception(e)
            return render(request, '404.html')
        return render(
            request, 'prediction_detail.html',
            {'prediction': transform_prediction(prediction), 'detailed': True}
        )


def truncate_with_ellipsis(text: str, max_len: int) -> str:
    if len(text) > max_len:
        return text[:max_len - 3] + "..."
    return text


class Predictions(ViewWithAPIClient):
    def get(self, request):
        try:
            predictions = self.api_client.get_predictions()
        except Exception as e:
            logging.exception(e)
            return render(request, 'predictions.html', {'predictions': []})

        data = [transform_prediction(prediction) for prediction in predictions]
        res = []
        for prediction in data:
            res.append({
                'id': prediction['id'],
                'created_at': str(prediction['created_at']).replace('T', ' ')[:16],
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


class RatePrediction(ViewWithAPIClient):
    def post(self, request):
        payload = json.loads(request.body.decode('utf-8'))
        try:
            prediction = self.api_client.rate_prediction(payload['prediction_id'], payload['rating'])
        except Exception as e:
            logging.exception(e)
            return JsonResponse({'message': 'Failed to rate the prediction. Please try again later.'}, status=500)
        rating = 'Positive' if prediction.rating is True else 'Negative' if prediction.rating is False else 'Unknown'
        return JsonResponse({'message': 'Prediction has been rated successfully.', 'rating': rating})


def transform_prediction(prediction: PredictionResponse):
    return {
        'id': prediction.id,
        'created_at': prediction.createdAt,
        'rating': 'Positive' if prediction.rating is True else 'Negative' if prediction.rating is False else 'Unknown',
        'text': prediction.predictionText,
        'label': 'Informative' if prediction.informative is True else 'Non-Informative',
        'confidence': prediction.confidence,
        # we only want to associate those that do not belong to a link
        'entities': transform_entities(
            [entity for entity in prediction.entities if entity.linkId is None]
        ),
        'links': transform_links(
            prediction.links, [entity for entity in prediction.entities if entity.linkId is not None]
        ),
    }


def transform_entities(entities: list[Entity]):
    res = {'locations': [], 'date_time': [], 'entities': [], 'other': []}
    for entity in entities:
        match entity.entityTypeEnumValue:
            case 'PLACE':
                target = 'locations'
            case 'DATE' | 'TIME':
                target = 'date_time'
            case 'ENTITY':
                target = 'entities'
            case 'OTHER':
                target = 'other'
            case _:
                logging.warning(f"Unknown entity type: {entity.entityTypeEnumValue}")
                continue
        res[target].append(entity.name)

    return res


def transform_links(links: list[Link], entities: list[Entity]):
    return [{
        'link': link.finalUrl,
        'title': link.title,
        'published_at': link.publishedAt,
        'has_entities': any(entity.linkId == link.id for entity in entities),
        'entities': transform_entities([entity for entity in entities if entity.linkId == link.id]),
        'description': link.text,
    } for link in links]
