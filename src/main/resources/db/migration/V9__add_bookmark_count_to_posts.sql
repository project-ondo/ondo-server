ALTER TABLE posts
    ADD COLUMN bookmark_count BIGINT NOT NULL DEFAULT 0;

UPDATE posts p
INNER JOIN (
    SELECT post_id, COUNT(*) AS cnt
    FROM bookmarks
    GROUP BY post_id
) b ON p.id = b.post_id
SET p.bookmark_count = b.cnt;
