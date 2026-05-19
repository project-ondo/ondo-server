SET @sql = IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE()
       AND TABLE_NAME = 'user_notification_setting'
       AND COLUMN_NAME = 'last_modified_at') = 0,
    'ALTER TABLE user_notification_setting ADD COLUMN last_modified_at DATETIME(6) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
