CREATE SCHEMA IF NOT EXISTS task;

DROP TABLE IF EXISTS task.process_document_mapping;
DROP TABLE IF EXISTS task.definition;

CREATE TABLE task.process_document_mapping
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    created_at       TIMESTAMP,
    modified_at      TIMESTAMP,
    version          INTEGER,
    process_name     VARCHAR DEFAULT NULL,
    document_type    VARCHAR DEFAULT NULL
);

CREATE TABLE task.definition
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    created_at     TIMESTAMP,
    modified_at    TIMESTAMP,
    version        INTEGER,
    type           VARCHAR DEFAULT NULL,
    code           VARCHAR DEFAULT NULL,
    display_value  VARCHAR DEFAULT NULL,
    short_display_value VARCHAR DEFAULT NULL,
    display_order          INTEGER DEFAULT NULL,
    is_active      BOOLEAN DEFAULT FALSE,
    CONSTRAINT uc_type_code UNIQUE (type, code)
);
