package com.example.familymenu.menu;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!memory")
public class JdbcFamilyMenuRepository implements FamilyMenuRepository {
    private final JdbcTemplate jdbc;

    public JdbcFamilyMenuRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean isCustomMenu(Long familyId) {
        List<Boolean> values = jdbc.query("SELECT custom_menu FROM families WHERE id = ?",
                new Object[]{familyId}, (rs, rowNum) -> rs.getBoolean(1));
        return !values.isEmpty() && Boolean.TRUE.equals(values.get(0));
    }

    @Override
    public void setCustomMenu(Long familyId, boolean customMenu) {
        jdbc.update("UPDATE families SET custom_menu = ? WHERE id = ?", customMenu, familyId);
    }
}
