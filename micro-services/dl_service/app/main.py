import logging

from fastapi import FastAPI

from app.extractor import Extractor
from app.routes import router

app = FastAPI()

app.include_router(router)


@app.on_event("startup")
def prepare_model():
    logging.info("Preparing model")
    Extractor()
    logging.info("Model ready")


@app.get("/")
def root():
    return {"status": "Model ready"}
