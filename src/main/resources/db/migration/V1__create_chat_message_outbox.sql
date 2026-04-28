CREATE TABLE chat_message_outbox
(
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    message_id       BIGINT      NOT NULL,
    room_public_id   BINARY(16)  NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at       DATETIME(6) NOT NULL,
    last_modified_at DATETIME(6),
    processed_at     DATETIME(6),
    PRIMARY KEY (id),
    INDEX idx_outbox_status_created (status, created_at)
);