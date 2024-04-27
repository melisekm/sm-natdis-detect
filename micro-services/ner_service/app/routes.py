from fastapi import APIRouter

from app.model import NER
from app.schemas import TextRequest, NERResponse, AnnotatedEntityResponse

router = APIRouter()


@router.post("/analyze")
def predict(request: TextRequest, add_html_highlight: bool = False) -> list[NERResponse]:
    """
    Performs named entity recognition on the input text.
    In response returns 1. - object containing list of annotated entities, the corresponding original text, original
    label provided by the model, and guessed label based on predefined rules of grouping e.g. group person and
    organisation under 'ENTITY - {PERSON | ORG}' 2. - HTML formatted text with highlighted entities 3. - dictionary
    containing groups of entities e.g. {'DATE': ['2021-01-01', '2021-01-02'], 'TIME': ['12:00', '13:00']}.
    Html Highligh is not returned by default, but can be requested by setting add_html_highlight to True.
    It is a standalone fully functional HTML code with tags that can be used to display the entities in a web page.
    """
    return handle_ner(request.data, add_html_highlight)

def handle_ner(data: str|list[str], add_html_highlight: bool = False) -> list[NERResponse]:
    if isinstance(data, str):
        data = [data]
    results = []
    for text in data:
        result = NER.instance.analyze(text)
        results.append(
            NERResponse(
                ents=[
                    AnnotatedEntityResponse(
                        text=ent.text, guessed_label=ent.guessed_label, original_label=ent.original_label
                    )
                    for ent in result.ents
                ],
                html_highlight=result.html_highlight if add_html_highlight else None,
                groups=result.groups,
            )
        )
    return results
