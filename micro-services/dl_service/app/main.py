import logging

from fastapi import FastAPI

from app.extractor import Extractor
from app.routes import router

app = FastAPI(
    title="AASS Download Service",
    description="Extracts articles from URLs",
    version="0.1.0",
)

app.include_router(router)


@app.on_event("startup")
def prepare_extractor():
    logging.info("Preparing extractor")
    Extractor()
    logging.info("Extractor ready")


@app.get("/")
def root():
    return {"status": "Extractor ready"}
