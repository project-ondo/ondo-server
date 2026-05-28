CREATE TABLE reports (
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    reporter_public_id BINARY(16) NOT NULL,
    target_type        VARCHAR(20) NOT NULL,
    target_id          BIGINT NOT NULL,
    description        TEXT NOT NULL,
    status             VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at         DATETIME(6) NOT NULL,
    last_modified_at   DATETIME(6),
    PRIMARY KEY (id),
    INDEX idx_reports_target (target_type, target_id),
    INDEX idx_reports_reporter (reporter_public_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
