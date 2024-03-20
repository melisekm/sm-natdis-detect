from fastapi import APIRouter

from app.extractor import Extractor
from app.schemas import TextRequest, ExtractionResult, URLResponse

router = APIRouter()


@router.post("/download")
def download(request: TextRequest) -> list[ExtractionResult]:
    if isinstance(request.data, str):
        request.data = [request.data]
    return [Extractor.instance.download(url) for url in request.data]


@router.post("/extract_urls")
def extract_urls(request: TextRequest) -> list[URLResponse]:
    if isinstance(request.data, str):
        request.data = [request.data]
    return [URLResponse(urls=Extractor.instance.extract_urls(text)) for text in request.data]
