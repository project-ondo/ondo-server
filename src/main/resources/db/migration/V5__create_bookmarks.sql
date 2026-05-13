CREATE TABLE bookmarks
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT      NOT NULL,
    post_id          BIGINT      NOT NULL,
    created_at       DATETIME(6) NOT NULL,
    last_modified_at DATETIME(6) NOT NULL,
    CONSTRAINT uk_bookmarks_user_post UNIQUE (user_id, post_id),
    CONSTRAINT fk_bookmarks_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_bookmarks_post FOREIGN KEY (post_id) REFERENCES posts (id)
);
