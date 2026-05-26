ALTER TABLE posts
    ADD COLUMN bookmark_count BIGINT NOT NULL DEFAULT 0;

UPDATE posts p
SET p.bookmark_count = (
    SELECT COUNT(*) FROM bookmarks b WHERE b.post_id = p.id
);
