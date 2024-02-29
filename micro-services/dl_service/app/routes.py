from typing import List

from fastapi import APIRouter

from app.model import Extractor
from app.schemas import TextRequest, ExtractionResponse, URLResponse

router = APIRouter()


@router.post("/download")
def predict(request: TextRequest) -> List[ExtractionResponse]:
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for url in request.data:
        result = Extractor.instance.download(url)
        results.append(
            ExtractionResponse(
                ...
            )
        )
    return results


@router.post("/extract_urls")
def extract_urls(request: TextRequest) -> List[URLResponse]:
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = Extractor.instance.extract_urls(text)
        results.append(URLResponse(...))
    return results

