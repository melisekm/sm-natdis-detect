import logging

from django.shortcuts import redirect

from dcesm.connectors.api_client import ExpiredTokenError, UnauthorizedError


class AuthMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        try:
            if 'login' not in request.path and '/static/' not in request.path:
                if 'token' not in request.session:
                    request.session['login_message'] = "To continue, please log in."
                    return redirect('login')

            response = self.get_response(request)
            return response
        except ExpiredTokenError:
            request.session['login_message'] = "Your session has expired. Please log in again."
            return redirect('login')
        except UnauthorizedError:
            request.session['login_message'] = "You are not authorized to access this page. Please log in."
            return redirect('login')
        except Exception as e:
            logging.exception(e)
            request.session['login_message'] = "To continue, please log in."
            return redirect('login')
