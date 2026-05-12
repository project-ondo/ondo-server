SET @idx = (
    SELECT INDEX_NAME
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'users'
      AND COLUMN_NAME = 'display_name'
      AND NON_UNIQUE = 0
    LIMIT 1
);
SET @sql = IF(@idx IS NOT NULL,
    CONCAT('ALTER TABLE users DROP INDEX `', @idx, '`'),
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;