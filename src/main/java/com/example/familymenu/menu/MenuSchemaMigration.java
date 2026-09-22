package com.example.familymenu.menu;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!memory")
public class MenuSchemaMigration implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public MenuSchemaMigration(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        addColumn("dishes", "family_id", "ALTER TABLE dishes ADD COLUMN family_id BIGINT NULL");
        addIndex("dishes", "idx_dishes_family_id", "ALTER TABLE dishes ADD KEY idx_dishes_family_id (family_id)");
        addColumn("families", "custom_menu", "ALTER TABLE families ADD COLUMN custom_menu BOOLEAN NOT NULL DEFAULT FALSE");
    }

    private void addColumn(String table, String column, String sql) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class, table, column);
        if (count == null || count.intValue() == 0) jdbc.execute(sql);
    }

    private void addIndex(String table, String index, String sql) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND INDEX_NAME = ?",
                Integer.class, table, index);
        if (count == null || count.intValue() == 0) jdbc.execute(sql);
    }
}
