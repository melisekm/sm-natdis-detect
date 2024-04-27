from typing import List

from fastapi import APIRouter

from app.model import NaturalDisBert
from app.schemas import TextRequest, PredictionResponse, ProcessingResponse

router = APIRouter()


@router.post("/predict")
def predict(request: TextRequest) -> List[PredictionResponse]:
    """
    Makes an prediction based on the input text. Input text is a social media post that is or is not
    informative to a natural disaster. Optionally accepts list of posts and returns list of predictions.
    The predictions consist of label named prediction result: either "Informative" or "Not informative",
    """
    return handle_prediction(request.data)


def handle_prediction(data: list[str] | str) -> List[PredictionResponse]:
    if isinstance(data, str):
        data = [data]
    results = []
    for text in data:
        result = NaturalDisBert.instance.infer(text)
        results.append(
            PredictionResponse(
                label=result.label, binary_label=result.binary_label, confidence=result.confidence
            )
        )
    return results


@router.post("/process_text")
def process_text(request: TextRequest, tokenize: bool = False) -> List[ProcessingResponse]:
    """
    Performs processing to a form that the model sees before tokenizing and making a prediction.
    Optionally also tokenize text, then each response is a list of tokens.
    For example if input contains '#liveupdates', after preprocessing hashtag is removed and
    the text is converted to lowercase. If tokenization is enabled, the text is split into tokens.
    See tokenize endpoint.
    """
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = NaturalDisBert.instance.process_text(text)
        if tokenize:
            result = NaturalDisBert.instance.tokenize(result)
        results.append(ProcessingResponse(processed_text=result))
    return results


@router.post("/tokenize")
def tokenize(request: TextRequest) -> List[ProcessingResponse]:
    """
    Tokenize text. This is the last step before making a prediction. After tokenization, the text is
    converted to a list of ids that the model can understand.
    If a word is not found in models vocabulary, it is split into subwords.
    Example: word 'liveupdates' is split into 'live', '##up', '##dates'.
    """
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = NaturalDisBert.instance.tokenize(text)
        results.append(ProcessingResponse(processed_text=result))
    return results
