CREATE TABLE user_suspensions (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    user_public_id  BINARY(16) NOT NULL,
    report_id       BIGINT NOT NULL,
    suspended_at    DATETIME(6) NOT NULL,
    suspended_until DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_user_suspensions_user (user_public_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;