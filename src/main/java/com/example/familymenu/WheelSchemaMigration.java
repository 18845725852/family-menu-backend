package com.example.familymenu;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!memory")
public class WheelSchemaMigration implements ApplicationRunner {
    private final JdbcTemplate jdbc;

    public WheelSchemaMigration(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!tableExists("family_wheel_restaurants")) {
            jdbc.execute("CREATE TABLE family_wheel_restaurants ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "family_id BIGINT NOT NULL,"
                    + "name VARCHAR(20) NOT NULL,"
                    + "sort INT NOT NULL DEFAULT 0,"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (id),"
                    + "KEY idx_family_wheel_restaurants_family_id (family_id),"
                    + "CONSTRAINT fk_family_wheel_restaurants_family FOREIGN KEY (family_id) REFERENCES families (id) ON DELETE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        }
        if (!tableExists("family_wheel_spins")) {
            jdbc.execute("CREATE TABLE family_wheel_spins ("
                    + "id BIGINT NOT NULL AUTO_INCREMENT,"
                    + "family_id BIGINT NOT NULL,"
                    + "user_id BIGINT NOT NULL,"
                    + "nickname VARCHAR(50) NOT NULL,"
                    + "avatar_url VARCHAR(500) NULL,"
                    + "restaurant_name VARCHAR(50) NOT NULL,"
                    + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (id),"
                    + "KEY idx_family_wheel_spins_family_id (family_id, id),"
                    + "CONSTRAINT fk_family_wheel_spins_family FOREIGN KEY (family_id) REFERENCES families (id) ON DELETE CASCADE,"
                    + "CONSTRAINT fk_family_wheel_spins_user FOREIGN KEY (user_id) REFERENCES users (id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        }
    }

    private boolean tableExists(String table) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?",
                Integer.class, table);
        return count != null && count.intValue() > 0;
    }
}
