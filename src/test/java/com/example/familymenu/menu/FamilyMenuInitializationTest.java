package com.example.familymenu.menu;

import com.example.familymenu.category.repository.InMemoryCategoryRepository;
import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.repository.InMemoryDishRepository;
import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.repository.InMemoryFamilyRepository;
import com.example.familymenu.order.repository.InMemoryOrderRepository;
import com.example.familymenu.restaurant.repository.InMemoryFamilyWheelRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FamilyMenuInitializationTest {
    private InMemoryDishRepository dishes;
    private FamilyMenuService service;
    private Family family;

    @BeforeEach
    void setup() {
        dishes = new InMemoryDishRepository();
        InMemoryFamilyMenuRepository menus = new InMemoryFamilyMenuRepository();
        InMemoryOrderRepository orders = new InMemoryOrderRepository();
        InMemoryFamilyRepository families = new InMemoryFamilyRepository(dishes, orders, menus, new InMemoryFamilyWheelRepository());
        family = families.createForUser("测试家庭", 10L);
        service = new FamilyMenuService(menus, families, dishes, new InMemoryCategoryRepository(), orders);
    }

    @Test
    void copiesOnlyAvailableDefaultsAndPreservesDishContent() {
        dishes.save(new Dish(null, "新菜", "家常菜", "介绍", "步骤", "/uploads/dish.jpg", true, 3, null));
        dishes.save(new Dish(null, "停用菜", "家常菜", null, null, null, false, 4, null));
        dishes.save(new Dish(null, "别组菜", "家常菜", null, null, null, true, 5, 99L));
        List<Dish> defaults = dishes.findAvailable(null);
        FamilyMenuResponse response = service.setCustomMenu(family.getId(), 10L, true);
        assertTrue(response.isCustomMenu());
        List<Dish> copies = dishes.findAvailableByFamily(family.getId());
        assertEquals(defaults.size(), copies.size());
        for (int i = 0; i < defaults.size(); i++) {
            Dish original = defaults.get(i);
            Dish copy = copies.get(i);
            assertNotEquals(original.getId(), copy.getId());
            assertEquals(original.getName(), copy.getName());
            assertEquals(original.getCategory(), copy.getCategory());
            assertEquals(original.getDescription(), copy.getDescription());
            assertEquals(original.getRecipe(), copy.getRecipe());
            assertEquals(original.getImageUrl(), copy.getImageUrl());
            assertEquals(original.getSort(), copy.getSort());
            assertEquals(family.getId(), copy.getFamilyId());
        }
    }

    @Test
    void reopeningPreservesExistingEditsAndIds() {
        service.setCustomMenu(family.getId(), 10L, true);
        List<Long> ids = dishes.findAvailableByFamily(family.getId()).stream().map(Dish::getId).collect(Collectors.toList());
        Dish first = dishes.findById(ids.get(0)).get();
        dishes.save(new Dish(first.getId(), "改名后的菜", first.getCategory(), "编辑内容", first.getRecipe(), first.getImageUrl(), true, first.getSort(), family.getId()));
        assertFalse(service.setCustomMenu(family.getId(), 10L, false).isCustomMenu());
        service.setCustomMenu(family.getId(), 10L, true);
        assertEquals(ids, dishes.findAvailableByFamily(family.getId()).stream().map(Dish::getId).collect(Collectors.toList()));
        assertEquals("改名后的菜", dishes.findById(ids.get(0)).get().getName());
    }

    @Test
    void outsidersAndInvalidFlagsCannotInitializeMenu() {
        assertThrows(BusinessException.class, () -> service.setCustomMenu(family.getId(), 20L, true));
        assertThrows(BusinessException.class, () -> service.setCustomMenu(family.getId(), 10L, null));
        assertEquals(0, dishes.countByFamily(family.getId()));
    }
}
