from typing import Literal

from pydantic import BaseModel, Field, field_validator, validator
import datetime as dt

EntityType = Literal['PLACE', 'ENTITY', 'OTHER', 'DATE', 'TIME']


class Link(BaseModel):
    id: int
    originUrl: str
    finalUrl: str
    text: str
    html: str
    title: str
    otherInfo: dict = Field(default_factory=dict)
    domain: str
    publishedAt: dt.datetime
    extractedAt: dt.datetime | None


class Entity(BaseModel):
    id: int
    name: str
    entityTypeEnumKey: EntityType
    entityTypeEnumValue: EntityType
    predictionId: int
    linkId: int | None
    createdAt: dt.datetime

    @field_validator('entityTypeEnumValue', mode='before')
    def validate_entity_type(cls, value):
        # its the smme but not uppercase, we do not care about that
        return value.upper()


class PredictionResponse(BaseModel):
    id: int
    informative: bool  # True for informative, False for non-informative
    confidence: float
    predictionText: str
    createdAt: dt.datetime
    updatedAt: dt.datetime | None
    rating: None | bool  # True for positive, False for negative, null for unknown
    links: list[Link] = Field(default_factory=list)
    entities: list[Entity] = Field(default_factory=list)

    @field_validator('confidence', mode='after')
    def round_confidence(cls, value):
        return round(value, 3)
