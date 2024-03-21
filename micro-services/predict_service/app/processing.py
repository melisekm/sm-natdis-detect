import re

from cleantext import clean
from emoji.unicode_codes import UNICODE_EMOJI_ENGLISH  # noqa
from emoji.core import demojize


def preprocess(text_orig: str) -> str:
    text = normalize(text_orig)
    text = transformers_text_clean([text])[0]
    return text


def normalize(text):
    return clean(
        text,
        lower=False,  # don't lower case
        fix_unicode=True,  # fix various unicode errors
        to_ascii=True,  # transliterate to closest ASCII representation
        no_line_breaks=True,  # fully strip line breaks as opposed to only normalizing them
    )


_EMOJI_MAP_RAW = {
    ":)": "smile",
    ":-)": "smile",
    ";d": "wink",
    ":-E": "vampire",
    ":(": "sad",
    ":-(": "sad",
    ":-<": "sad",
    ":P": "raspberry",
    ":O": "surprised",
    ":-@": "shocked",
    ":@": "shocked",
    ":-$": "confused",
    ":\\": "annoyed",
    ":#": "mute",
    ":X": "mute",
    ":^)": "smile",
    ":-&": "confused",
    "$_$": "greedy",
    ":-!": "confused",
    ":-D": "smile",
    ":-0": "yell",
    "O.o": "confused",
    "<(-_-)>": "robot",
    "d[-_-]b": "dj",
    ":'-)": "sadsmile",
    ";)": "wink",
    ";-)": "wink",
    "O:-)": "angel",
    "O*-)": "angel",
    "(:-D": "gossip",
    "=^.^=": "cat",
}

_EMOJI_MAP_RAW_UPDATED = {k: f"<EMOJI {v}>" for k, v in _EMOJI_MAP_RAW.items()}
EMOJI_MAP = {k: f"<EMOJI {v.replace(':', '')}>" for k, v in UNICODE_EMOJI_ENGLISH.items()}
EMOJI_MAP.update(_EMOJI_MAP_RAW_UPDATED)


def replace_emojis(texts):
    def custom_handle_version(emj: str, _: dict) -> str:
        return EMOJI_MAP.get(emj, "")

    # set version to 0 to custom replace all emojis with custom_handle_version
    return [demojize(text, version=0, handle_version=custom_handle_version) for text in texts]


def transformers_text_clean(texts):
    # remove html tags
    texts = [re.sub(r"<.*?>", "", text) for text in texts]
    # remove RT @name:
    texts = [re.sub(r"RT @.*?:? ?", "", text, flags=re.IGNORECASE) for text in texts]
    # convert all @username to "AT_USER"
    texts = [re.sub("@[^\s]+", "<AT_USER>", text) for text in texts]
    # convert "#topic" to just "topic"
    texts = [re.sub(r"#([^\s]+)", r"\1", text) for text in texts]
    # change all emojis to <EMOJI name>
    texts = replace_emojis(texts)

    texts = [
        clean(
            text,
            lower=False,
            no_punct=False,
            no_urls=True,
            no_emails=True,
            no_phone_numbers=True,
            no_numbers=True,
            no_currency_symbols=True,
            no_emoji=False,
            replace_with_url="<URL>",
            replace_with_email="<EMAIL>",
            replace_with_phone_number="<PHONE>",
            replace_with_number="<NUMBER>",
            replace_with_currency_symbol="<CUR>",
            normalize_whitespace=False,
        )
        for text in texts
    ]
    return texts
