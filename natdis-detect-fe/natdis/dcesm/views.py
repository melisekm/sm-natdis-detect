import logging

from django.shortcuts import render, redirect
from django.views import View

from dcesm.connectors.keycloak_auth import keycloak_authenticate


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


class Predict(View):
    def get(self, request):
        return render(request, 'predict.html')


class Predictions(View):
    pass


class Entities(View):
    pass