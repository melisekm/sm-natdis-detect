# Frontend for Detection of Catastrophic Events from Social Media

## Setup

```bash
cd natdis
cp .env.example .env
```

### Env variables

```dotenv
API_HOST=http://proxy_entrypoint_service:8080
KEYCLOAK_AUTH_URL=https://<name>.<domain>/auth/realms/<REALM>/protocol/openid-connect/token
KEYCLOAK_CLIENT_ID=<CLIENT_ID>
KEYCLOAK_GRANT_TYPE=<GRANT_TYPE># e.g. password
```

### Start the app

```bash
docker-compose up
```