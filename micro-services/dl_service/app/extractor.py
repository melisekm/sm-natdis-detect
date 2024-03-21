from __future__ import annotations

import ast
import dataclasses
import logging
import re
import urllib.parse as urlparse
from datetime import datetime

import requests
from bs4 import BeautifulSoup
from newspaper import Article as NewspaperArticle
from urlextract import URLExtract

from app.schemas import ExtractionResult, ExtractionError

USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:105.0) Gecko/20171104 Firefox/105.0"
ACCEPT_LANGUAGE = "sk,cs;q=0.9,en-US;q=0.8,en;q=0.7,lv;q=0.6,ro;q=0.5,de;q=0.4,hu;q=0.3"
ACCEPT = (
    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,"
    "application/signed-exchange;v=b3;q=0.9"
)


@dataclasses.dataclass
class DownloadResult:
    final_url: str
    domain: str
    html: bytes


class Extractor:
    instance: 'Extractor' = None

    def __new__(cls):
        if not cls.instance:
            cls.instance = super().__new__(cls)
            cls.instance._extractor = NewspaperExtractor()
        return cls.instance

    def download(self, url: str) -> ExtractionResult | ExtractionError:
        return self._extractor.get_article(url)

    def extract_urls(self, text: str) -> list[str]:
        return self._extractor.extract_urls(text)


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

    def get_article(self, url: str) -> ExtractionResult | ExtractionError:
        """
        Abstract method for parsing a InputObject into an Article object.
        InputObject is a dict with keys 'url' or 'html or both.
        HTML is optional, if not present, it will be downloaded from the URL.
        Transforms a InputObject into an desired object through transform method.

        Args:
            url: URL of the article to be parsed
        Returns:
            Parsed Article object or Extraction Failure as a dict if parsing failed,
            or None if the article is not an Article
        """
        try:
            try:
                download_result = self._download(url)
                if isinstance(download_result, ExtractionError):
                    return download_result
                parsed_article = self.parse_with_newspaper(download_result.final_url, download_result.html)
            except Exception as exception:
                return ExtractionError(url=url, description="Unable to transform Article.", exception=str(exception))
            logging.debug(f"Parsing Article. URL: {url}")

            article = ExtractionResult(
                orig_url=url,
                final_url=download_result.final_url,
                extracted_at=datetime.now().isoformat(),
                published_at=str(parsed_article.publish_date) if parsed_article.publish_date else None,
                title=parsed_article.title,
                text=parsed_article.text,
                html=parsed_article.article_html,
                domain=download_result.domain,
                other_info=self.get_other_info(parsed_article)
            )
            logging.info(f"Article validated. URL: {url}. Final URL: {download_result.final_url}")
            return article
        except Exception as exception:
            return ExtractionError(url=url, description="Could not parse article", exception=str(exception))

    def parse_with_newspaper(self, url: str, html: bytes) -> NewspaperArticle:
        # html was already downloaded
        article = NewspaperArticle(url, keep_article_html=True)
        article.download(input_html=html)
        article.parse()
        return article

    def _download(self, url: str, redirect_count=0) -> DownloadResult | ExtractionError:
        logging.info(f"Downloading article from {url}")

        try:
            orig_url_domain = urlparse.urlparse(url).netloc
            response = self.session.get(url, headers=self.headers, timeout=15)
            response.raise_for_status()
            final_url = clean_url(get_final_url(response))
            final_url_domain = urlparse.urlparse(final_url).netloc
            if (
                    "twitter.com" not in final_url
                    and orig_url_domain == "t.co"
                    and redirect_count < self.max_redirect_count
                    and orig_url_domain != final_url_domain
            ):
                logging.info(f"{url} -> {final_url}")
                return self._download(final_url, redirect_count + 1)
            return DownloadResult(final_url, final_url_domain, response.content)
        except Exception as exception:
            return ExtractionError(url=url, description="Failed to download the article.", exception=str(exception))

    def get_other_info(self, article: NewspaperArticle) -> dict[str, str | list[str]]:
        other_info = {}
        if article.meta_keywords and any([keyword for keyword in article.meta_keywords if keyword.strip()]):
            other_info["meta_keywords"] = article.meta_keywords
        if article.tags:
            other_info["tags"] = list(article.tags)
        if article.authors:
            other_info["unprocessed_authors"] = article.authors
        return other_info

    def extract_urls(self, text: str) -> list[str]:
        return self.url_extractor.find_urls(text, only_unique=True)


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
