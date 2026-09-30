package com.example.familymenu.restaurant.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.family.domain.FamilyMember;
import com.example.familymenu.family.repository.FamilyRepository;
import com.example.familymenu.restaurant.domain.FamilyWheelSpin;
import com.example.familymenu.restaurant.domain.Restaurant;
import com.example.familymenu.restaurant.domain.UserWheelRestaurant;
import com.example.familymenu.restaurant.dto.FamilyWheelResponse;
import com.example.familymenu.restaurant.repository.FamilyWheelRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FamilyWheelServiceImpl implements FamilyWheelService {
    public static final int MIN_COUNT = 3;
    public static final int MAX_COUNT = 16;
    public static final int MAX_NAME_LENGTH = 20;
    public static final int RECENT_SPIN_LIMIT = 5;

    private final FamilyWheelRepository repository;
    private final FamilyRepository familyRepository;
    private final RestaurantService restaurantService;

    public FamilyWheelServiceImpl(FamilyWheelRepository repository, FamilyRepository familyRepository,
                                  RestaurantService restaurantService) {
        this.repository = repository;
        this.familyRepository = familyRepository;
        this.restaurantService = restaurantService;
    }

    public FamilyWheelResponse get(Long familyId, Long userId) {
        requireMember(familyId, userId);
        return view(familyId);
    }

    public List<FamilyWheelSpin> listSpins(Long familyId, Long userId) {
        requireMember(familyId, userId);
        return repository.findSpins(familyId, 0);
    }

    @Transactional
    public FamilyWheelResponse replace(Long familyId, Long userId, List<String> names) {
        requireMember(familyId, userId);
        List<String> cleaned = cleanNames(names);
        repository.deleteOptions(familyId);
        for (int i = 0; i < cleaned.size(); i++) {
            repository.insertOption(familyId, cleaned.get(i), i);
        }
        return view(familyId);
    }

    @Transactional
    public FamilyWheelSpin spin(Long familyId, Long userId, String name) {
        requireMember(familyId, userId);
        String cleaned = name == null ? "" : name.trim();
        if (cleaned.isEmpty()) throw new BusinessException("餐厅名称不能为空");
        List<UserWheelRestaurant> options = currentOptions(familyId);
        if (options.isEmpty()) throw new BusinessException("当前没有可抽取的餐厅");
        boolean matched = false;
        for (UserWheelRestaurant option : options) {
            if (cleaned.equals(option.getName())) {
                matched = true;
                break;
            }
        }
        if (!matched) throw new BusinessException("这个餐厅不在当前转盘里");
        FamilyMember member = findMember(familyId, userId);
        String nickname = member != null && member.getNickname() != null && !member.getNickname().trim().isEmpty()
                ? member.getNickname().trim() : "家人";
        if (nickname.length() > 50) nickname = nickname.substring(0, 50);
        String avatarUrl = member == null ? null : member.getAvatarUrl();
        return repository.insertSpin(familyId, userId, nickname, avatarUrl, cleaned, LocalDateTime.now());
    }

    private FamilyWheelResponse view(Long familyId) {
        List<UserWheelRestaurant> custom = repository.findOptions(familyId);
        boolean customized = !custom.isEmpty();
        List<UserWheelRestaurant> restaurants = customized ? custom : defaultOptions();
        return new FamilyWheelResponse(customized, restaurants, repository.findSpins(familyId, RECENT_SPIN_LIMIT));
    }

    private List<UserWheelRestaurant> currentOptions(Long familyId) {
        List<UserWheelRestaurant> custom = repository.findOptions(familyId);
        return custom.isEmpty() ? defaultOptions() : custom;
    }

    private List<UserWheelRestaurant> defaultOptions() {
        List<UserWheelRestaurant> options = new ArrayList<UserWheelRestaurant>();
        for (Restaurant restaurant : restaurantService.list(false)) {
            options.add(new UserWheelRestaurant(restaurant.getId(), restaurant.getName(), restaurant.getSort()));
        }
        return options;
    }

    private List<String> cleanNames(List<String> names) {
        List<String> cleaned = new ArrayList<String>();
        Set<String> seen = new HashSet<String>();
        if (names != null) {
            for (String raw : names) {
                String name = raw == null ? "" : raw.trim();
                if (name.isEmpty()) throw new BusinessException("餐厅名称不能为空");
                if (name.length() > MAX_NAME_LENGTH) throw new BusinessException("餐厅名称不能超过20个字");
                if (!seen.add(name)) throw new BusinessException("餐厅名称不能重复: " + name);
                cleaned.add(name);
            }
        }
        if (cleaned.size() < MIN_COUNT || cleaned.size() > MAX_COUNT) {
            throw new BusinessException("餐厅数量需要在3到16家之间");
        }
        return cleaned;
    }

    private void requireMember(Long familyId, Long userId) {
        if (familyId == null || !familyRepository.isMember(familyId, userId)) {
            throw new BusinessException("你不是该家庭成员");
        }
    }

    private FamilyMember findMember(Long familyId, Long userId) {
        for (FamilyMember member : familyRepository.findMembers(familyId)) {
            if (userId.equals(member.getUserId())) return member;
        }
        return null;
    }
}
