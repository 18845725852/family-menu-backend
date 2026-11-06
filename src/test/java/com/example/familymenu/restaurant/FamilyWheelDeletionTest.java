package com.example.familymenu.restaurant;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.family.repository.FamilyRepository;
import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.dto.FamilyWheelResponse;
import com.example.familymenu.restaurant.repository.InMemoryFamilyWheelRepository;
import com.example.familymenu.restaurant.repository.JdbcFamilyWheelRepository;
import com.example.familymenu.restaurant.service.FamilyWheelServiceImpl;
import com.example.familymenu.restaurant.service.RestaurantService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyWheelDeletionTest {
    private InMemoryFamilyWheelRepository repository;
    private FamilyWheelServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryFamilyWheelRepository();
        FamilyRepository families = mock(FamilyRepository.class);
        when(families.isMember(1L, 20L)).thenReturn(true);
        RestaurantService restaurants = mock(RestaurantService.class);
        when(restaurants.list(false)).thenReturn(Collections.emptyList());
        service = new FamilyWheelServiceImpl(repository, families, restaurants);
    }

    private FamilyWheelSpin insert(Long familyId) {
        return repository.insertSpin(familyId, 10L, "家人", null, "餐厅", LocalDateTime.now());
    }

    @Test
    void memberCanDeleteFamilyRecordAndRecentListIsReplenished() {
        for (int i = 0; i < 6; i++) insert(1L);
        Long id = repository.findSpins(1L, 1).get(0).getId();
        FamilyWheelResponse result = service.deleteSpin(1L, 20L, id);
        assertEquals(5, result.getSpins().size());
        assertTrue(result.getSpins().stream().noneMatch(spin -> spin.getId().equals(id)));
        assertEquals(5, service.listSpins(1L, 20L).size());
        assertThrows(BusinessException.class, () -> service.deleteSpin(1L, 20L, id));
    }

    @Test
    void outsidersCannotDeleteAndAnotherFamilysRecordCannotBeDeleted() {
        Long own = insert(1L).getId();
        Long other = insert(2L).getId();
        assertThrows(BusinessException.class, () -> service.deleteSpin(1L, 99L, own));
        assertThrows(BusinessException.class, () -> service.deleteSpin(1L, 20L, other));
        assertEquals(1, repository.findSpins(1L, 0).size());
        assertEquals(1, repository.findSpins(2L, 0).size());
    }

    @Test
    void lastRecordCanBeDeleted() {
        Long id = insert(1L).getId();
        assertTrue(service.deleteSpin(1L, 20L, id).getSpins().isEmpty());
        assertTrue(service.listSpins(1L, 20L).isEmpty());
    }

    @Test
    void jdbcDeletionAlwaysScopesTheRecordToItsFamily() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        when(jdbc.update("DELETE FROM family_wheel_spins WHERE family_id=? AND id=?", 1L, 7L)).thenReturn(1);
        JdbcFamilyWheelRepository persisted = new JdbcFamilyWheelRepository(jdbc);
        assertTrue(persisted.deleteSpin(1L, 7L));
        assertFalse(persisted.deleteSpin(2L, 7L));
        verify(jdbc).update("DELETE FROM family_wheel_spins WHERE family_id=? AND id=?", 1L, 7L);
    }
}
