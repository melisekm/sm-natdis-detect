from typing import List

from fastapi import APIRouter

from app.model import NER
from app.schemas import TextRequest, NERResponse, AnnotatedEntityResponse

router = APIRouter()


@router.post("/analyze")
def predict(request: TextRequest) -> List[NERResponse]:
    if isinstance(request.data, str):
        request.data = [request.data]
    results = []
    for text in request.data:
        result = NER.instance.analyze(text)
        results.append(
            NERResponse(
                ents=[
                    AnnotatedEntityResponse(
                        text=ent.text, guessed_label=ent.guessed_label, original_label=ent.original_label
                    )
                    for ent in result.ents
                ],
                html_highlight=result.html_highlight,
                groups=result.groups,
            )
        )
    return results
