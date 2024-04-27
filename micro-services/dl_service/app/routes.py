from fastapi import APIRouter

from app.extractor import Extractor
from app.schemas import TextRequest, ExtractionResult, URLResponse

router = APIRouter()


@router.post("/download")
def download(request: TextRequest) -> list[ExtractionResult]:
    """
    Download each article from provided list of URLs. If the input is a string, it is treated as a single URL.
    The input should contain fully qualified URLs. and nothing else. Get them from the extract_urls endpoint.
    """
    if isinstance(request.data, str):
        request.data = [request.data]
    return [Extractor.instance.download(url) for url in request.data]


@router.post("/extract_urls")
def extract_urls(request: TextRequest) -> list[URLResponse]:
    """
    Extracts URLs from the provided list of texts. If the input is a string, it is treated as a single text.
    Example: `"This is a text with a URL https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/"`
    Result would be `{"urls": ["https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/"]}`
    """
    if isinstance(request.data, str):
        request.data = [request.data]
    return [URLResponse(urls=Extractor.instance.extract_urls(text)) for text in request.data]


def handle_dl(data: str | list[str]) -> list[ExtractionResult]:
    if isinstance(data, str):
        data = [data]
    return [
        Extractor.instance.download(url)
        for text in data
        for url in Extractor.instance.extract_urls(text)
    ]
