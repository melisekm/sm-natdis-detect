import logging
import os

from fastapi import FastAPI

from app import get_path
from app.model import NaturalDisBert
from app.routes import router

app = FastAPI(
    title="AASS Predict Service",
    description="Predicts whetever the social media "
                "post is informative in regards to natural disasters",
    version="0.1.0",
)

app.include_router(router)


@app.on_event("startup")
def prepare_model():
    logging.info("Preparing model")
    model_local_path = os.getenv("MODEL_LOCAL_PATH")
    model_hf_path = os.getenv("MODEL_HF_PATH")
    if model_local_path:
        model_path = get_path("models", model_local_path, raise_on_not_found=True)
    elif model_hf_path:
        model_path = model_hf_path
    else:
        raise ValueError(
            "No model path provided. "
            "Please set either MODEL_LOCAL_PATH or MODEL_HF_PATH environment variable."
        )
    NaturalDisBert(model_path, os.getenv("DEVICE", "cpu"))
    logging.info("Model ready")


@app.get("/")
def root():
    return {"status": "Model ready"}
