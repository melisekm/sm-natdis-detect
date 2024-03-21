from typing import Literal
import pydantic
from pydantic import BaseModel


class TextRequest(BaseModel):
    data: str | list[str]


class PredictionResponse(BaseModel):
    label: Literal["Informative", "Not Informative"]
    binary_label: Literal[0, 1]
    confidence: pydantic.confloat(ge=0, le=1)


class ProcessingResponse(BaseModel):
    processed_text: str | list[str]
