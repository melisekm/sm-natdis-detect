from typing import List

from fastapi import APIRouter

from app.model import NaturalDisBert
from app.schemas import TextRequest, PredictionResponse, ProcessingResponse

router = APIRouter()


@router.post("/predict")
def predict(request: TextRequest) -> List[PredictionResponse]:
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = NaturalDisBert.instance.infer(text)
        results.append(
            PredictionResponse(
                label=result.label, binary_label=result.binary_label, confidence=result.confidence
            )
        )
    return results


@router.post("/process_text")
def process_text(request: TextRequest, tokenize: bool = False) -> List[ProcessingResponse]:
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
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = NaturalDisBert.instance.tokenize(text)
        results.append(ProcessingResponse(processed_text=result))
    return results
