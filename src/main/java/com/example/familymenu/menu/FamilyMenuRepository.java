package com.example.familymenu.menu;

public interface FamilyMenuRepository {
    boolean isCustomMenu(Long familyId);

    void setCustomMenu(Long familyId, boolean customMenu);
}
