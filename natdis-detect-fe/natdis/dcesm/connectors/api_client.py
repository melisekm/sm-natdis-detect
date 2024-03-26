# import datetime
# import logging
# import time
#
# import jwt
# import requests
# from django.conf import settings
#
# from connectors.api_properties import APIProperties
#
#
#
#
#
# class APIClient:
#     def __init__(self):
#         self.api_properties = APIProperties()
#         self.jwt_authorizer = JWTAuthorizer()
#
#     def is_authorized(self, token, token_expiration):
#         pass
#         # if token is None:
#         #     return False
#         #
#         #
#         # if self.token is None \
#         #         or self.token_expiration is None \
#         #         or datetime.datetime.now().timestamp() > self.token_expiration:
#         #     return False
#         #
#         # return True
#
#     # def get_authorization_token(self):
#     #     if not self.is_authorized():
#     #         self.authorize()
#     #
#     #     return self.token
#     #
#     # def get_request_headers(self):
#     #     return {
#     #         'Authorization': 'Bearer ' + self.get_authorization_token()
#     #     }