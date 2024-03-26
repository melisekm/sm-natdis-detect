from django.conf import settings


class APIProperties:
    def __init__(self):
        self.api_host = settings.API_HOST

        ...
