import dataclasses
import logging
from typing import List

import torch
from transformers import AutoModelForSequenceClassification, AutoTokenizer, pipeline
from app.processing import preprocess


@dataclasses.dataclass
class Output:
    label: str
    binary_label: int
    confidence: float


class NaturalDisBert:
    instance: 'NaturalDisBert' = None

    def __new__(cls, path: str, device: str = "cpu"):
        if not cls.instance:
            cls.instance = super().__new__(cls)
            cls.instance.model = InferenceModel(
                trained_model_path=path, device=device
            )
        return cls.instance

    def infer(self, text: str) -> Output:
        return self.model.infer(text)

    def process_text(self, text: str) -> str:
        return preprocess(text)

    def tokenize(self, text: str) -> List[str]:
        return self.model.pipeline.tokenizer.tokenize(
            text, truncation=self.model.truncation,
            padding=self.model.padding,
            max_length=self.model.max_length
        )


class InferenceModel:
    def __init__(
            self,
            trained_model_path=None,
            device='cpu',
            truncation=True,
            padding=False,
            max_length=512,
    ):
        if device == "cpu":
            device = torch.device("cpu")
        elif device == "cuda":
            if not torch.cuda.is_available():
                raise ValueError("GPU not available")
            device = torch.device("cuda")

        logging.info("Loading model...")
        model = AutoModelForSequenceClassification.from_pretrained(trained_model_path)

        logging.info("Loading tokenizer...")
        tokenizer = AutoTokenizer.from_pretrained(trained_model_path)

        self.truncation = truncation or tokenizer.init_kwargs.get("truncation", truncation)
        self.padding = padding or tokenizer.init_kwargs.get("padding", padding)
        self.max_length = max_length or tokenizer.model_max_length

        self.pipeline = pipeline(
            "text-classification", model=model, tokenizer=tokenizer, device=device
        )

    def infer(self, text_orig: str) -> Output:
        text = preprocess(text_orig)
        prediction = self.pipeline(
            text,
            **{"padding": self.padding, "truncation": self.truncation, "max_length": self.max_length}
        )
        prediction = prediction[0]

        return Output(
            prediction["label"],
            self.pipeline.model.config.label2id[prediction["label"]],
            prediction["score"]
        )
