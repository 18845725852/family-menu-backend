package com.example.familymenu.restaurant.repository;

import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
public class InMemoryFamilyWheelRepository implements FamilyWheelRepository {
    private final ConcurrentMap<Long, List<UserWheelRestaurant>> options = new ConcurrentHashMap<Long, List<UserWheelRestaurant>>();
    private final ConcurrentMap<Long, List<FamilyWheelSpin>> spins = new ConcurrentHashMap<Long, List<FamilyWheelSpin>>();
    private final AtomicLong optionIds = new AtomicLong();
    private final AtomicLong spinIds = new AtomicLong();

    public synchronized List<UserWheelRestaurant> findOptions(Long familyId) {
        return new ArrayList<UserWheelRestaurant>(options.getOrDefault(familyId, Collections.<UserWheelRestaurant>emptyList()));
    }

    public synchronized void deleteOptions(Long familyId) {
        options.remove(familyId);
    }

    public synchronized UserWheelRestaurant insertOption(Long familyId, String name, int sort) {
        UserWheelRestaurant saved = new UserWheelRestaurant(optionIds.incrementAndGet(), name, sort);
        List<UserWheelRestaurant> list = options.get(familyId);
        if (list == null) {
            list = new ArrayList<UserWheelRestaurant>();
            options.put(familyId, list);
        }
        list.add(saved);
        return saved;
    }

    public synchronized FamilyWheelSpin insertSpin(Long familyId, Long userId, String nickname, String avatarUrl,
                                                   String restaurantName, LocalDateTime createdAt) {
        FamilyWheelSpin saved = new FamilyWheelSpin(spinIds.incrementAndGet(), userId, nickname, avatarUrl, restaurantName, createdAt);
        List<FamilyWheelSpin> list = spins.get(familyId);
        if (list == null) {
            list = new ArrayList<FamilyWheelSpin>();
            spins.put(familyId, list);
        }
        list.add(saved);
        return saved;
    }

    public synchronized List<FamilyWheelSpin> findSpins(Long familyId, int limit) {
        List<FamilyWheelSpin> list = spins.getOrDefault(familyId, Collections.<FamilyWheelSpin>emptyList());
        List<FamilyWheelSpin> result = new ArrayList<FamilyWheelSpin>();
        int cap = limit > 0 ? limit : list.size();
        for (int i = list.size() - 1; i >= 0 && result.size() < cap; i--) {
            result.add(list.get(i));
        }
        return result;
    }
}
