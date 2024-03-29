alter table link
    rename column link to origin_url;

alter table link
    add column final_url varchar(255);

alter table link
    rename column created_at to extracted_at;

alter table link
    rename column body to text;

alter table link
    alter column text type text;

alter table link
    rename column raw_body to html;

alter table link
    alter column html type text;

alter table link
    add column domain varchar(255);