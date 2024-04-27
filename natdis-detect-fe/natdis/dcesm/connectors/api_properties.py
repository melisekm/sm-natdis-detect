from typing import Literal


class APIProperties:
    def __init__(self, api_host: str):
        self.api_host = api_host

    def create_prediction_url(self, type_: Literal['microservices', 'camunda']):
        match type_:
            case 'microservices':
                return self.api_host + "/prediction/flow"
            case 'camunda':
                return self.api_host + "/camunda/startProcessByKey/PredictionMainFlow"
            case _:
                raise ValueError(f"Invalid prediction type: {type_}")

    def get_prediction_url(self, prediction_id: int):
        return self.api_host + f"/prediction/{prediction_id}"

    def get_rate_prediction_url(self, prediction_id: int, rating: str):
        return self.api_host + f"/prediction/{prediction_id}/rate/{rating}"

    def find_all_predictions_url(self):
        return self.api_host + "/predictions"
