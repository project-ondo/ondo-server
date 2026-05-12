package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class V4__remove_display_name_unique extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();

        String indexName = null;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT INDEX_NAME FROM information_schema.STATISTICS " +
                "WHERE TABLE_SCHEMA = DATABASE() " +
                "AND TABLE_NAME = 'users' " +
                "AND COLUMN_NAME = 'display_name' " +
                "AND NON_UNIQUE = 0 LIMIT 1")) {
            if (rs.next()) {
                indexName = rs.getString("INDEX_NAME");
            }
        }

        if (indexName != null) {
            String escapedIndexName = indexName.replace("`", "``");
            try (Statement drop = connection.createStatement()) {
                drop.execute("ALTER TABLE users DROP INDEX `" + escapedIndexName + "`");
            }
        }
    }
}