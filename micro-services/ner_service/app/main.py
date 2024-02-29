import logging
import os

from fastapi import FastAPI

from app.model import NER
from app.routes import router

app = FastAPI()

app.include_router(router)


@app.on_event("startup")
def prepare_model():
    logging.info("Preparing model")
    NER(os.getenv("NER_MODEL_NAME", 'en_core_web_trf'))
    logging.info("Model ready")


@app.get("/")
def root():
    return {"status": "Model ready"}
