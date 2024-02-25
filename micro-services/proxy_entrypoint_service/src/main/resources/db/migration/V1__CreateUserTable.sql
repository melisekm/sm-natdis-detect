create table internal_user (
  id serial primary key,
  name varchar(255),
  email varchar(255),
  keycloak_id varchar(255)
);