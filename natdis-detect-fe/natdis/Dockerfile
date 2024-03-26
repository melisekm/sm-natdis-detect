FROM python:3.11.8

RUN mkdir /usr/local/nvm
ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION 20.12.0
RUN curl https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash \
    && . $NVM_DIR/nvm.sh \
    && nvm install $NODE_VERSION \
    && nvm alias default $NODE_VERSION \
    && nvm use default

ENV PATH $NVM_DIR/versions/node/v$NODE_VERSION/bin:$PATH

ENV PYTHONUNBUFFERED 1

RUN mkdir /code

WORKDIR /code

COPY requirements.txt /code/
RUN pip install -r requirements.txt --no-cache-dir

COPY package.json /code/
RUN npm i

COPY . /code/
RUN python manage.py migrate

#ENTRYPOINT python manage.py collectstatic --no-input && gunicorn webapp.wsgi -b 0.0.0.0:8000 --access-logfile - --error-logfile - --enable-stdio-inheritance --log-level debug
ENTRYPOINT python manage.py runserver 0.0.0.0:8000