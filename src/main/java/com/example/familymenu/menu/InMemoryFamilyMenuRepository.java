package com.example.familymenu.menu;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
public class InMemoryFamilyMenuRepository implements FamilyMenuRepository {
    private final ConcurrentMap<Long, Boolean> flags = new ConcurrentHashMap<Long, Boolean>();

    @Override
    public boolean isCustomMenu(Long familyId) {
        return Boolean.TRUE.equals(flags.get(familyId));
    }

    @Override
    public void setCustomMenu(Long familyId, boolean customMenu) {
        flags.put(familyId, customMenu);
    }
}
