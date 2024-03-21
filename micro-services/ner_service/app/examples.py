ner_examples = [
    {
        "ents": [
            {
                "text": "Uttarakhand",
                "guessed_label": "PLACE - GPE",
                "original_label": "GPE"
            },
            {
                "text": "September 1 to 3",
                "guessed_label": "DATE",
                "original_label": "DATE"
            },
            {
                "text": "MET Department",
                "guessed_label": "PLACE - ORG",
                "original_label": "ORG"
            },
            {
                "text": "METdepartment",
                "guessed_label": "PLACE - ORG",
                "original_label": "ORG"
            }
        ],
        "html_highlight": None,
        "groups": {
            "PLACE": [
                "MET Department",
                "METdepartment",
                "Uttarakhand"
            ],
            "DATE": [
                "September 1 to 3"
            ]
        }
    },
    {
        "ents": [
            {
                "text": "John",
                "guessed_label": "ENTITY - PERSON",
                "original_label": "PERSON"
            },
            {
                "text": "the slovak national museum",
                "guessed_label": "PLACE - FAC",
                "original_label": "FAC"
            }
        ],
        "html_highlight": None,
        "groups": {
            "ENTITY": [
                "John"
            ],
            "PLACE": [
                "the slovak national museum"
            ]
        }
    },
    {
        "ents": [],
        "html_highlight": None,
        "groups": {}
    },
    {
        "ents": [
            {
                "text": "Australia",
                "guessed_label": "PLACE - GPE",
                "original_label": "GPE"
            },
            {
                "text": "Brisbane",
                "guessed_label": "PLACE - GPE",
                "original_label": "GPE"
            }
        ],
        "html_highlight": None,
        "groups": {
            "PLACE": [
                "Australia",
                "Brisbane"
            ]
        }
    }
]