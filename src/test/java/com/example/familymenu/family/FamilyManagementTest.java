package com.example.familymenu.family;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.dish.domain.Dish;
import com.example.familymenu.dish.repository.InMemoryDishRepository;
import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.domain.FamilyMember;
import com.example.familymenu.family.repository.InMemoryFamilyRepository;
import com.example.familymenu.family.service.FamilyServiceImpl;
import com.example.familymenu.menu.InMemoryFamilyMenuRepository;
import com.example.familymenu.order.domain.Order;
import com.example.familymenu.order.repository.InMemoryOrderRepository;
import com.example.familymenu.restaurant.repository.InMemoryFamilyWheelRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyManagementTest {
    private InMemoryDishRepository dishes;
    private InMemoryOrderRepository orders;
    private InMemoryFamilyMenuRepository menus;
    private InMemoryFamilyWheelRepository wheel;
    private InMemoryFamilyRepository families;
    private FamilyServiceImpl service;
    private Family family;

    @BeforeEach
    void setUp() {
        dishes = new InMemoryDishRepository();
        orders = new InMemoryOrderRepository();
        menus = new InMemoryFamilyMenuRepository();
        wheel = new InMemoryFamilyWheelRepository();
        families = new InMemoryFamilyRepository(dishes, orders, menus, wheel);
        service = new FamilyServiceImpl(families);
        family = service.createForUser("原家庭", 10L);
        families.addMemberUserId(family.getId(), 20L, "成员", "MEMBER");
        families.addMemberUserId(family.getId(), 30L, "管理员", "ADMIN");
    }

    @Test
    void ownerCanRenameAndMembershipIsPreserved() {
        Family renamed = service.rename(family.getId(), "  新家庭  ", 10L);
        assertEquals("新家庭", renamed.getName());
        assertEquals(10L, renamed.getOwnerUserId());
        assertEquals(3, service.members(family.getId()).size());
        assertEquals("新家庭", service.mineByUserId(20L).get(0).getName());
        assertEquals("新家庭", service.rename(family.getId(), "新家庭", 10L).getName());
    }

    @Test
    void membersAdminsAndOutsidersCannotRenameOrDelete() {
        for (Long userId : new Long[]{20L, 30L, 40L}) {
            assertThrows(BusinessException.class, () -> service.rename(family.getId(), "越权", userId));
            assertThrows(BusinessException.class, () -> service.delete(family.getId(), userId));
        }
        assertEquals("原家庭", service.get(family.getId()).getName());
        assertEquals(3, service.members(family.getId()).size());
    }

    @Test
    void rejectsEmptyAndOverlongNames() {
        for (String name : new String[]{null, "", "   ", String.join("", Collections.nCopies(31, "家"))}) {
            assertThrows(BusinessException.class, () -> service.rename(family.getId(), name, 10L));
        }
        assertEquals("原家庭", service.get(family.getId()).getName());
    }

    @Test
    void nonOwnersCanOnlyLeaveThemselves() {
        Long memberId = member(20L).getId();
        Long adminId = member(30L).getId();
        assertThrows(BusinessException.class, () -> service.removeMember(family.getId(), memberId, 30L));
        assertThrows(BusinessException.class, () -> service.removeMember(family.getId(), adminId, 20L));
        assertThrows(BusinessException.class, () -> service.removeMember(family.getId(), memberId, 40L));
        service.removeMember(family.getId(), memberId, 20L);
        service.removeMember(family.getId(), adminId, 30L);
        assertEquals(1, service.members(family.getId()).size());
        assertTrue(service.mineByUserId(20L).isEmpty());
    }

    @Test
    void ownerCanRemoveOthersButNobodyCanRemoveOwner() {
        Long ownerId = member(10L).getId();
        for (Long userId : new Long[]{10L, 20L, 30L, 40L}) {
            assertThrows(BusinessException.class, () -> service.removeMember(family.getId(), ownerId, userId));
        }
        service.removeMember(family.getId(), member(20L).getId(), 10L);
        service.removeMember(family.getId(), member(30L).getId(), 10L);
        assertEquals(1, service.members(family.getId()).size());
    }

    @Test
    void roleEndpointCannotBypassOwnerPermissions() {
        Long memberId = member(20L).getId();
        assertThrows(BusinessException.class, () -> service.updateMemberRole(family.getId(), memberId, "ADMIN", 20L));
        assertThrows(BusinessException.class, () -> service.updateMemberRole(family.getId(), memberId, "OWNER", 10L));
        assertThrows(BusinessException.class, () -> service.updateMemberRole(family.getId(), member(10L).getId(), "MEMBER", 10L));
        assertEquals("ADMIN", service.updateMemberRole(family.getId(), memberId, "ADMIN", 10L).getRole());
    }

    @Test
    void deletingFamilyRemovesItsDataAndPreservesOtherFamiliesAndDefaults() {
        Family other = service.createForUser("另一个家庭", 50L);
        Long dishId = seedData(family.getId());
        Long otherDishId = seedData(other.getId());
        service.delete(family.getId(), 10L);
        assertFalse(families.findById(family.getId()).isPresent());
        assertTrue(families.findMembers(family.getId()).isEmpty());
        assertTrue(service.mineByUserId(20L).isEmpty());
        assertTrue(orders.findAllByFamilyId(family.getId()).isEmpty());
        assertFalse(dishes.findById(dishId).isPresent());
        assertFalse(menus.isCustomMenu(family.getId()));
        assertTrue(wheel.findOptions(family.getId()).isEmpty());
        assertTrue(wheel.findSpins(family.getId(), 10).isEmpty());
        assertTrue(families.findById(other.getId()).isPresent());
        assertEquals(1, orders.findAllByFamilyId(other.getId()).size());
        assertTrue(dishes.findById(otherDishId).isPresent());
        assertEquals(2, dishes.findAvailable(null).size());
        assertTrue(menus.isCustomMenu(other.getId()));
        assertEquals(1, wheel.findOptions(other.getId()).size());
        assertEquals(1, wheel.findSpins(other.getId(), 10).size());
    }

    @Test
    void missingFamilyAndCrossFamilyMemberIdsAreRejected() {
        Family other = service.createForUser("其他家庭", 50L);
        Long otherOwnerId = families.findMembers(other.getId()).get(0).getId();
        assertThrows(BusinessException.class, () -> service.removeMember(family.getId(), otherOwnerId, 10L));
        service.delete(family.getId(), 10L);
        assertThrows(BusinessException.class, () -> service.delete(family.getId(), 10L));
        assertThrows(BusinessException.class, () -> service.rename(family.getId(), "已删除", 10L));
    }

    private FamilyMember member(Long userId) {
        return families.findMembers(family.getId()).stream()
                .filter(member -> member.getUserId().equals(userId)).findFirst().get();
    }

    private Long seedData(Long familyId) {
        Dish dish = dishes.save(new Dish(null, "测试菜品", "家常菜", null, null, null, true, 0, familyId));
        orders.save(new Order(null, familyId, 10L, "测试用户", Collections.emptyList(), null, LocalDateTime.now()));
        menus.setCustomMenu(familyId, true);
        wheel.insertOption(familyId, "测试餐厅", 0);
        wheel.insertSpin(familyId, 10L, "测试用户", null, "测试餐厅", LocalDateTime.now());
        return dish.getId();
    }
}
