CREATE DATABASE gift_certificates_db ENCODING 'UTF8';

CREATE USER gift_certificates_client
    PASSWORD 'postgres';

CREATE TABLE gift_certificate
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(128) NOT NULL,
    description      TEXT         NOT NULL,
    price            BIGINT       NOT NULL,
    duration         INT          NOT NULL,
    create_date      TIMESTAMP    NOT NULL,
    last_update_date TIMESTAMP    NOT NULL
);

CREATE TABLE tag
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) UNIQUE NOT NULL
);

CREATE TABLE gift_certificate_tag
(
    gift_certificate_id BIGINT NOT NULL REFERENCES gift_certificate (id) ON DELETE CASCADE,
    tag_id              BIGINT NOT NULL REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (gift_certificate_id, tag_id)
);

GRANT SELECT, INSERT, UPDATE, DELETE
    ON ALL TABLES IN SCHEMA public
    TO gift_certificates_client;

GRANT USAGE, SELECT
    ON ALL SEQUENCES IN SCHEMA public
    TO gift_certificates_client;
