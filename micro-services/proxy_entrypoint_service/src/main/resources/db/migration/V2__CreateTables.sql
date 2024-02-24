CREATE TABLE prediction (
                              "id" integer PRIMARY KEY NOT NULL,
                              "informative" bool NOT NULL,
                              "confidence" float NOT NULL,
                              "prediction_text" varchar(255),
                              "created_at" timestamp,
                              "updated_at" timestamp
);

CREATE TABLE entity (
                          "id" integer PRIMARY KEY NOT NULL,
                          "name" varchar(255) NOT NULL,
                          "entity_type_enum_key" varchar(50) NOT NULL,
                          "prediction_id" integer NOT NULL,
                          "link_id" integer,
                          "created_at" timestamp
);

CREATE TABLE entity_type_enum (
                                    "enum_key" varchar(50) PRIMARY KEY NOT NULL,
                                    "enum_value" varchar(50) NOT NULL
);

CREATE TABLE link (
                        "id" integer PRIMARY KEY NOT NULL,
                        "link" varchar(255) NOT NULL,
                        "body" varchar(255),
                        "raw_body" varchar(255),
                        "title" varchar(255),
                        "other_info" jsonb,
                        "published_at" timestamp,
                        "created_at" timestamp
);

CREATE TABLE prediction_links (
                                    "prediction_id" integer NOT NULL,
                                    "link_id" integer NOT NULL
);

ALTER TABLE entity ADD FOREIGN KEY ("prediction_id") REFERENCES prediction ("id");

ALTER TABLE entity ADD FOREIGN KEY ("entity_type_enum_key") REFERENCES entity_type_enum ("enum_key");

ALTER TABLE prediction_links ADD FOREIGN KEY ("prediction_id") REFERENCES prediction ("id");

ALTER TABLE prediction_links ADD FOREIGN KEY ("link_id") REFERENCES link ("id");

ALTER TABLE entity ADD FOREIGN KEY ("link_id") REFERENCES link ("id");
