from __future__ import annotations

import ast
import dataclasses
import hashlib
import logging
import re
import urllib.parse as urlparse
from dataclasses import dataclass
from datetime import datetime

import requests
from bs4 import BeautifulSoup
from newspaper import Article as NewspaperArticle
from urlextract import URLExtract

USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:105.0) Gecko/20171104 Firefox/105.0"
ACCEPT_LANGUAGE = "sk,cs;q=0.9,en-US;q=0.8,en;q=0.7,lv;q=0.6,ro;q=0.5,de;q=0.4,hu;q=0.3"
ACCEPT = (
    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,"
    "application/signed-exchange;v=b3;q=0.9"
)


@dataclasses.dataclass
class Output:
    orig_url: str
    final_url: str | None
    hash_id: str
    extracted_at: str
    published_at: str | None
    title: str
    text: str
    html: str
    other_info: dict[str] | None = None


@dataclasses.dataclass
class URL:
    ...


class Extractor:
    instance: 'Extractor' = None

    def __new__(cls):
        if not cls.instance:
            cls.instance = super().__new__(cls)
            cls.instance._extractor = NewspaperExtractor()
        return cls.instance

    def download(self, url: str) -> Output:
        return self._extractor.get_article({"url": url})

    def extract_urls(self, text: str) -> list[URL]:
        return self._extractor.extract_urls(text)


@dataclass
class ExtractionError:
    url: str
    description: str | None = None
    exception: str | None = None

    def __post_init__(self):
        logging.error(f"ExtractionError: {self.description} | URL: {self.url} | Exception: {self.exception}")


class NewspaperExtractor:
    """
    General newspaper parser for parsing articles from HTML or JSON
    Tries to extract body from HTML either directly from HTML file or after downloading it first
    """

    def __init__(self, max_redirect_count=3):
        super().__init__()
        self.headers = {
            "User-Agent": USER_AGENT,
            "Accept-Language": ACCEPT_LANGUAGE,
            "Accept": ACCEPT,
        }
        self.session = requests.Session()
        self.max_redirect_count = max_redirect_count
        logging.getLogger("urllib3").setLevel(logging.ERROR)
        self.url_extractor = URLExtract()

    def get_article(self, input_object: dict) -> Output | ExtractionError:
        """
        Abstract method for parsing a InputObject into an Article object.
        InputObject is a dict with keys 'url' or 'html or both.
        HTML is optional, if not present, it will be downloaded from the URL.
        Transforms a InputObject into an desired object through transform method.

        Args:
            input_object: data to be extracted, contains at least url or html or both
        Returns:
            Parsed Article object or Extraction Failure as a dict if parsing failed,
            or None if the article is not an Article
        """
        if not isinstance(input_object, dict):
            raise ValueError("Input object must be a dict")
        try:
            try:
                data = self.transform(input_object)
            except Exception as exception:
                return ExtractionError(input_object["url"], "Unable to transform Article.", str(exception))
            if isinstance(data, ExtractionError):
                return data
            if not data:
                return ExtractionError(input_object["url"], "Unable to transform Article.", "No data")
            logging.debug(f"Parsing Article. URL: {input_object['url']}")

            article = Output(
                orig_url=input_object["url"],
                final_url=input_object.get("final_url"),
                hash_id=hashlib.md5(input_object["url"].encode("UTF-8")).hexdigest(),
                extracted_at=datetime.now().isoformat(),
                published_at=str(data.publish_date) if data.publish_date else None,
                title=data.title,
                text=data.text,
                html=data.article_html,
                other_info=self.get_other_info(data)
            )
            logging.info(f"Article validated. URL: {input_object['final_url']}")
            return article
        except Exception as exception:
            return ExtractionError(input_object["url"], "Could not parse article", str(exception))

    def transform(self, data: dict) -> NewspaperArticle:
        if data.get("html"):
            # html was already downloaded
            article = NewspaperArticle(data.get("final_url", data.get("url")), keep_article_html=True)
            article.download(input_html=data.get("html"))
            article.parse()
            logging.debug("NewspaperArticle parsed from HTML")
            return article

        if data.get("url"):
            orig_url = data.get("url")
            download_result = self.download(orig_url)
            if isinstance(download_result, ExtractionError):
                return download_result
            data.update(download_result)
            return self.transform(data)

        raise ValueError("Input object must contain at least url or html or both")

    def download(self, url: str, redirect_count=0) -> dict | ExtractionError:
        result = {}
        try:
            orig_url_domain = urlparse.urlparse(url).netloc
            logging.debug(f"Downloading article from {url}")
            response = self.session.get(url, headers=self.headers, timeout=15)
            response.raise_for_status()
            final_url = clean_url(get_final_url(response))
            final_url_domain = urlparse.urlparse(final_url).netloc
            result["final_url"] = final_url
            if (
                    "twitter.com" not in final_url
                    and orig_url_domain == "t.co"
                    and redirect_count < self.max_redirect_count
                    and orig_url_domain != final_url_domain
            ):
                logging.info(f"{url} -> {final_url}")
                return self.download(final_url, redirect_count + 1)
            result["html"] = response.content
        except Exception as exception:
            return ExtractionError(url, "Failed to download the article.", str(exception))
        return result

    def get_other_info(self, data: NewspaperArticle) -> dict[str]:
        other_info = {}
        if data.meta_keywords and data.meta_keywords != [""]:
            other_info["meta_keywords"] = data.meta_keywords
        if data.tags:
            other_info["tags"] = list(data.tags)
        if data.authors:
            other_info["unprocessed_author"] = ",".join(data.authors)
        return other_info

    def extract_urls(self, text: str) -> list[URL]:
        return url_extractor.find_urls(text)


def clean_url(url: str) -> str:
    url = url.strip()
    if url.startswith(("'", '"')) and url.endswith(("'", '"')):
        url = ast.literal_eval(url)
    url = urlparse.unquote(url)
    query_params = urlparse.parse_qs(url)
    if "url" in query_params:
        url = query_params["url"][0]
    return url


def get_final_url(response: requests.Response) -> str:
    content = BeautifulSoup(response.content, "lxml")
    meta = content.find_all("meta")
    for meta_obj in meta:
        if meta_obj.get("http-equiv") == "refresh":
            match = re.search("URL=(.*)", meta_obj["content"])
            if match:
                return match.group(1)
    return response.url
