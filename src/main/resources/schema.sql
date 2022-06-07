CREATE SCHEMA IF NOT EXISTS task;

DROP TABLE IF EXISTS task.process_document_mapping;

CREATE TABLE task.process_document_mapping
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    process_name     VARCHAR,
    document_type    VARCHAR
);