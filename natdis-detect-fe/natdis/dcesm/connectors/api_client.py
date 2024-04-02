import datetime
import logging
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
        self.api_properties = APIProperties(self.api_host)
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

    def predict(self, text: str) -> PredictionResponse | None:
        try:
            r = self.make_base_post_request(
                self.api_properties.create_prediction_url(),
                data=text
            )
            r.raise_for_status()
            idx = r.text
            r = self.make_base_get_request(
                self.api_properties.get_prediction_url(idx)
            )
            r.raise_for_status()
            return PredictionResponse(**r.json())
        except requests.exceptions.RequestException as e:
            logging.exception(f"Failed to make a prediction. Network error has occurred. {e}")
            raise e
