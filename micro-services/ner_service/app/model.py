from collections import defaultdict
import dataclasses
import logging
import re
from typing import Dict, List

import spacy
from spacy import displacy

ner_grps_cols = ["DATE", "TIME", "ENTITY", "PLACE", "QUANTITY", "OTHER"]


@dataclasses.dataclass
class AnnotatedEntity:
    text: str
    guessed_label: str
    original_label: str


@dataclasses.dataclass
class Output:
    ents: List[AnnotatedEntity]
    html_highlight: str
    groups: Dict[str, List[str]]


class NER:
    instance: "NER" = None

    def __new__(cls, model_name: str):
        if not cls.instance:
            cls.instance = super().__new__(cls)
            cls.instance.model = SpacyNERModel(model_name)
        return cls.instance

    def analyze(self, text: str) -> Output:
        return self.model.analyze(text)


class SpacyNERModel:
    def __init__(self, model_name: str):
        logging.info("Loading NER...")
        try:
            self.nlp = spacy.load(model_name)
        except OSError:
            spacy.cli.download(model_name)
            self.nlp = spacy.load(model_name)

        self.options = {
            "ents": [
                "PERSON",
                "NORP",
                "FAC",
                "ORG",
                "GPE",
                "LOC",
                "EVENT",
                "LANGUAGE",
                "DATE",
                "TIME",
                "QUANTITY",
            ]
        }

    def analyze(self, text: str) -> Output:
        text = re.sub(r"#(\S+)", r"\1", text)
        doc = self.nlp(text)
        html = displacy.render(doc, style="ent", page=True, options=self.options)
        html = (
            "<div style='max-width:100%; max-height:360px; overflow:auto'>"
            + html
            + "</div>"
        )
        ents = [
            AnnotatedEntity(ent.text, self.guess_label(ent.label_), ent.label_)
            for ent in doc.ents
        ]
        # group ents by label
        predicted_labels = [ent.guessed_label for ent in ents]
        groups = {
            label: [ent.text for ent in ents if ent.guessed_label == label]
            for label in set(predicted_labels)
        }
        res_groups = defaultdict(list)
        for k, v in groups.items():
            ix = ner_grps_cols.index(k.split()[0])
            res_groups[ner_grps_cols[ix]].extend(v)

        return Output(ents, html, res_groups)

    def guess_label(self, label: str) -> str:
        if label in {"DATE", "TIME"}:
            return label
        if label in {"PERSON", "NORP"}:
            return f"ENTITY - {label}"
        if label in {"FAC", "ORG", "GPE", "LOC", "EVENT", "LANGUAGE"}:
            return f"PLACE - {label}"
        if label in {"QUANTITY"}:
            return f"QUANTITY - {label}"
        return f"OTHER - {label}"
