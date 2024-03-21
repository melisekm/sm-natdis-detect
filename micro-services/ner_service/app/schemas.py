from pydantic import BaseModel, Field

from app.examples import ner_examples


class TextRequest(BaseModel):
    data: str | list[str]


class AnnotatedEntityResponse(BaseModel):
    text: str
    guessed_label: str
    original_label: str


class NERResponse(BaseModel):
    ents: list[AnnotatedEntityResponse] = Field(default_factory=list)
    html_highlight: str | None = None
    groups: dict[str, list[str]] = Field(default_factory=dict)

    model_config = {
        'json_schema_extra': {
            'examples': ner_examples
        }
    }
