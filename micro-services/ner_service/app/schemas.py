from typing import Union, List, Dict

from pydantic import BaseModel, Field


class TextRequest(BaseModel):
    data: Union[str, List[str]]


class AnnotatedEntityResponse(BaseModel):
    text: str
    guessed_label: str
    original_label: str


class NERResponse(BaseModel):
    ents: List[AnnotatedEntityResponse] = Field(default_factory=list)
    html_highlight: str
    groups: Dict[str, List[str]] = Field(default_factory=dict)
