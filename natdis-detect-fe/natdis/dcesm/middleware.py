import logging

from django.shortcuts import redirect


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
        except Exception as e:
            logging.exception(e)
            request.session['login_message'] = "To continue, please log in."
            return redirect('login')
