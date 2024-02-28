from typing import Union, List

from pydantic import BaseModel


class TextRequest(BaseModel):
    data: Union[str, List[str]]


class PredictionResponse(BaseModel):
    label: str
    binary_label: int
    confidence: float


class ProcessingResponse(BaseModel):
    processed_text: Union[str, List[str]]
