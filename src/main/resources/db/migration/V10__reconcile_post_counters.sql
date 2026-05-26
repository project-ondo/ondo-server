UPDATE posts p
LEFT JOIN (
    SELECT post_id, COUNT(*) AS cnt
    FROM post_likes
    GROUP BY post_id
) l ON p.id = l.post_id
SET p.like_count = COALESCE(l.cnt, 0);

UPDATE posts p
LEFT JOIN (
    SELECT post_id, COUNT(*) AS cnt
    FROM comments
    WHERE status = 'ACTIVE'
    GROUP BY post_id
) c ON p.id = c.post_id
SET p.comment_count = COALESCE(c.cnt, 0);

UPDATE posts p
LEFT JOIN (
    SELECT post_id, COUNT(*) AS cnt
    FROM bookmarks
    GROUP BY post_id
) b ON p.id = b.post_id
SET p.bookmark_count = COALESCE(b.cnt, 0);
