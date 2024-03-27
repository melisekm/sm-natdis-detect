FROM python:3.11.8

ENV PYTHONUNBUFFERED 1

RUN mkdir /code
WORKDIR /code

COPY requirements.txt /code/
RUN pip install -r requirements.txt --no-cache-dir

COPY . /code/
RUN python manage.py migrate

#ENTRYPOINT python manage.py collectstatic --no-input && gunicorn webapp.wsgi -b 0.0.0.0:8000 --access-logfile - --error-logfile - --enable-stdio-inheritance --log-level debug
ENTRYPOINT python manage.py runserver 0.0.0.0:8000