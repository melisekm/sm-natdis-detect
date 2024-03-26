import logging

import jwt
# import jwt
# from cryptography.hazmat.backends import default_backend
# from cryptography.hazmat.primitives import serialization
from django.conf import settings
from django.core.exceptions import PermissionDenied
from django.http import HttpResponse
from django.shortcuts import redirect


# from jwt import DecodeError


class AuthMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response
        # self.central_storage_client = CentralStorageClient()

    def __call__(self, request):
        # return self.get_response(request)
        try:
            if 'login' not in request.path and '/static/' not in request.path:
                if 'token' not in request.session:
                    request.session['login_message'] = "To continue, please log in."
                    return redirect('login')

            response = self.get_response(request)
            return response
        except Exception as e:
            logging.exception(e)
            request.session['login_message'] = "To continue, please log in."
            return redirect('login')

    #     try:
    #         if 'login' not in request.path and '/static/' not in request.path:
    #             self.handle_auth(request)
    #
    #         response = self.get_response(request)
    #         return response
    #     except DecodeError:
    #         new_key = self.central_storage_client.public_key()
    #         if self.key == new_key:
    #             request.session['login_message'] = "Invalid session token."
    #             return redirect('login')
    #         else:
    #             self.key = new_key
    #             try:
    #                 self.handle_auth(request)
    #                 response = self.get_response(request)
    #                 return response
    #             except DecodeError:
    #                 request.session['login_message'] = "Invalid session token."
    #                 return redirect('login')
    #     except PermissionDenied:
    #         request.session['login_message'] = "You do not have permission to view this page."
    #         return redirect('login')
    #     except Exception as e:
    #         logging.exception(e)
    #         request.session['login_message'] = "To continue, please log in."
    #         return redirect('login')
    #
        # token = request.session['token']
        # decoded_token = jwt.decode(token, options={'verify_signature': False}, verify=False, algorithms=['RS256'])
        # request.session['user'] = decoded_token
        # try:

        # token = request.session['token']
        # decoded_token = jwt.decode(
        #     token,
        #     options={'verify_signature': False},
        #     verify=False,
        #     algorithms=['RS256']
        # )
        # request.session['user'] = decoded_token
