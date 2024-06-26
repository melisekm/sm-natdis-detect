import datetime
import logging

from confluent_kafka import Producer

from typing import Literal

import requests
from django.conf import settings

from dcesm.connectors.api_properties import APIProperties
from dcesm.schemas import PredictionResponse


class ExpiredTokenError(Exception):
    pass


class UnauthorizedError(Exception):
    pass


class APIClient:
    def __init__(self):
        self.api_host = settings.API_HOST
        self.kafka_prediction_topic = settings.KAFKA_PREDICTION_TOPIC
        self.api_properties = APIProperties(self.api_host)
        self.kafka_producer = Producer({'bootstrap.servers': settings.KAFKA_URI})
        self.token = None
        self.token_expiration = None
        self.user = None

    def is_authorized(self):
        if self.token is None:
            raise UnauthorizedError
        if self.token_expiration is None \
                or datetime.datetime.now().timestamp() > self.token_expiration:
            return False
        return True

    def get_authorization_token(self):
        if not self.is_authorized():
            raise ExpiredTokenError
        return self.token

    def get_request_headers(self):
        return {
            'Authorization': 'Bearer ' + self.get_authorization_token()
        }

    def make_base_post_request(self, url, json=None, data=None):
        return requests.post(
            url,
            headers=self.get_request_headers(),
            json=json,
            data=data
        )

    def make_base_get_request(self, url, params=None):
        return requests.get(
            url,
            headers=self.get_request_headers(),
            params=params
        )

    def predict_microservices(self, text: str) -> PredictionResponse | None:
        r = self.make_base_post_request(
            self.api_properties.create_prediction_url('microservices'),
            data=text
        )
        r.raise_for_status()
        idx = r.text
        r = self.make_base_get_request(
            self.api_properties.get_prediction_url(idx)
        )
        r.raise_for_status()
        return PredictionResponse(**r.json())

    def predict_camunda(self, text: str) -> None:
        r = self.make_base_post_request(
            self.api_properties.create_prediction_url('camunda'),
            json={
                "variableList": [
                    {
                        "name": "text",
                        "value": text,
                    },
                    {
                        "name": "token",
                        "value": self.get_authorization_token(),
                    }
                ]
            }
        )
        r.raise_for_status()

    def predict_kafka(self, text: str) -> None:
        self.kafka_producer.produce(self.kafka_prediction_topic, text)
        self.kafka_producer.flush()

    def rate_prediction(self, prediction_id: int, rating: Literal['true', 'false']):
        r = self.make_base_post_request(
            self.api_properties.get_rate_prediction_url(prediction_id, rating)
        )
        r.raise_for_status()
        return PredictionResponse(**r.json())

    def get_prediction(self, prediction_id):
        r = self.make_base_get_request(
            self.api_properties.get_prediction_url(prediction_id)
        )
        r.raise_for_status()
        return PredictionResponse(**r.json())

    def get_predictions(self):
        r = self.make_base_get_request(
            self.api_properties.find_all_predictions_url()
        )
        r.raise_for_status()
        return [PredictionResponse(**p) for p in r.json()]
