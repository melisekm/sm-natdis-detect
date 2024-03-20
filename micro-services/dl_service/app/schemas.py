import logging

import pydantic
from pydantic import BaseModel

from app.examples import result_examples


class TextRequest(BaseModel):
    data: str | list[str]

    model_config = {
        'json_schema_extra': {
            'examples': [
                {
                    "data": "https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/"
                },
                {
                    "data": [
                        "https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/",
                        "https://www.snopes.com/fact-check/italian-pm-ancestral-rights//"
                    ]
                }
            ]
        }
    }


class ExtractionError(BaseModel):
    url: str
    description: str | None = None
    exception: str | None = None

    def __init__(self, **data):
        super().__init__(**data)
        logging.error(f"ExtractionError: {self.description} | URL: {self.url} | Exception: {self.exception}")

    model_config = {
        'json_schema_extra': {
            'examples': [
                {
                    "url": "https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/",
                    "description": "Unable to transform Article.",
                    "exception": "Exception: Article not found"
                }
            ]
        }
    }


class ExtractionResult(BaseModel):
    orig_url: str
    final_url: str | None
    extracted_at: str
    published_at: str | None
    title: str
    text: str
    html: str
    domain: str | None = None
    other_info: dict | None = pydantic.Field(default_factory=dict)

    model_config = {
        'json_schema_extra': {
            'examples': result_examples
        }
    }


class URLResponse(BaseModel):
    urls: list[str]

    model_config = {
        'json_schema_extra': {
            'examples': [
                {
                    "urls": [
                        "https://www.snopes.com/fact-check/japanese-american-pledge-allegiance/"
                    ]
                }
            ]
        }
    }
