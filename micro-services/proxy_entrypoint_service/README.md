# Proxy entrypoint - Central data storage
Communicates with microservices and stores returned data to postgresql database

## Tech Stack
- Java 17
- maven
- Keycloak
- Spring Boot
- Postgresql 16
## Setup

Make sure that the network `aass_network` exists or create it
`docker network create aass_network`

```bash
# fill out your env variables -> db credentials, exposed ports etc..
cp .env.example .env  
docker compose up -d
```

