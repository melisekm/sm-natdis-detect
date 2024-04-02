class APIProperties:
    def __init__(self, api_host: str):
        self.api_host = api_host

    def create_prediction_url(self):
        return self.api_host + "/prediction/flow"

    def get_prediction_url(self, prediction_id: int):
        return self.api_host + f"/prediction/{prediction_id}"

    def get_rate_prediction_url(self, prediction_id: int, rating: str):
        return self.api_host + f"/prediction/{prediction_id}/rate/{rating}"
