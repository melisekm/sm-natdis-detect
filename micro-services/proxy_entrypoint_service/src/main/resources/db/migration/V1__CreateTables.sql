CREATE TABLE internal_user (
                               id bigserial PRIMARY KEY,
                               name varchar(255),
                               email varchar(255),
                               keycloak_id varchar(255)
);

CREATE TABLE prediction (
                            id bigserial PRIMARY KEY NOT NULL,
                            informative bool NOT NULL,
                            confidence float NOT NULL,
                            prediction_text varchar(255),
                            created_at timestamp,
                            updated_at timestamp
);

CREATE TABLE entity_type_enum (
                                  enum_key varchar(50) PRIMARY KEY NOT NULL,
                                  enum_value varchar(50) NOT NULL
);

CREATE TABLE link (
                      id bigserial PRIMARY KEY NOT NULL,
                      link varchar(255) NOT NULL,
                      body varchar(255),
                      raw_body varchar(255),
                      title varchar(255),
                      other_info varchar(500),
                      published_at timestamp,
                      created_at timestamp
);

CREATE TABLE entity (
                        id bigserial PRIMARY KEY NOT NULL,
                        name varchar(255) NOT NULL,
                        entity_type_enum_key varchar(50) NOT NULL,
                        prediction_id bigint NOT NULL,
                        link_id bigint,
                        created_at timestamp,
                        FOREIGN KEY (prediction_id) REFERENCES prediction (id),
                        FOREIGN KEY (entity_type_enum_key) REFERENCES entity_type_enum (enum_key),
                        FOREIGN KEY (link_id) REFERENCES link (id)
);

CREATE TABLE prediction_links (
                                  prediction_id bigserial NOT NULL,
                                  link_id bigint NOT NULL,
                                  FOREIGN KEY (prediction_id) REFERENCES prediction (id),
                                  FOREIGN KEY (link_id) REFERENCES link (id)
);

