FROM python:3.11

COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt --no-cache-dir --extra-index-url https://download.pytorch.org/whl/cpu

COPY app /workspace/app
WORKDIR /workspace

ENTRYPOINT uvicorn app.main:app --reload --host 0.0.0.0 --port 8000