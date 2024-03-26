import logging
import time

import jwt
import requests
from django.conf import settings


def keycloak_authenticate(username: str, password: str) -> dict:
    retry = 0
    while retry < 3:
        login_data = {
            'username': username,
            'password': password,
            'client_id': settings.KEYCLOAK_CLIENT_ID,
            'grant_type': settings.KEYCLOAK_GRANT_TYPE,
        }
        try:
            r = requests.post(
                settings.KEYCLOAK_AUTH_URL,
                timeout=60,
                data=login_data
            )
            if r.status_code == 200:
                response = r.json()
                token = response['access_token']
                token_decoded = jwt.decode(
                    token,
                    options={'verify_signature': False},
                    verify=False,
                    algorithms=['RS256']
                )
                token_expiration = token_decoded.get('exp')
                logging.info("Successfully authorized to Keycloak API.")
                return {
                    "status": "success",
                    "token": token,
                    "decoded_token": token_decoded,
                    "token_expiration": token_expiration
                }
            if r.status_code == 401:
                return {
                    "status": "error",
                    "message": "Invalid username or password"
                }
            r.raise_for_status()
        except Exception as e:
            retry += 1
            logging.exception(f"Failed to authorize to {settings.KEYCLOAK_AUTH_URL} API. Retrying in 1 second. "
                              f"Retry {retry}/3 "
                              f"Reason: {e}")
            time.sleep(1)
    raise Exception(f"Could not authorize to central storage API. After 3 retries.")
