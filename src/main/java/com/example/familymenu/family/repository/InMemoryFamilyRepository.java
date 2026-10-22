package com.example.familymenu.family.repository;

import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.domain.FamilyMember;
import com.example.familymenu.dish.repository.InMemoryDishRepository;
import com.example.familymenu.order.repository.InMemoryOrderRepository;
import com.example.familymenu.menu.InMemoryFamilyMenuRepository;
import com.example.familymenu.restaurant.repository.InMemoryFamilyWheelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("memory")
@RequiredArgsConstructor
public class InMemoryFamilyRepository implements FamilyRepository {
    private final InMemoryDishRepository dishRepository;
    private final InMemoryOrderRepository orderRepository;
    private final InMemoryFamilyMenuRepository menuRepository;
    private final InMemoryFamilyWheelRepository wheelRepository;
    private final AtomicLong ids = new AtomicLong();
    private final Map<Long, Family> families = new HashMap<Long, Family>();
    private final Map<Long, List<FamilyMember>> members = new HashMap<Long, List<FamilyMember>>();
    private final AtomicLong memberIds = new AtomicLong();

    public synchronized Family createForUser(String name, Long userId) {
        long familyId = ids.incrementAndGet();
        Family family = new Family(familyId, name, userId);
        families.put(familyId, family);
        List<FamilyMember> familyMembers = new ArrayList<FamilyMember>();
        familyMembers.add(new FamilyMember(memberIds.incrementAndGet(), familyId, userId, "微信用户", "OWNER"));
        members.put(familyId, familyMembers);
        return family;
    }

    public Optional<Family> findById(Long id) { return Optional.ofNullable(families.get(id)); }
    public List<Family> findByUserNickname(String nickname) {
        List<Family> result = new ArrayList<Family>();
        for (Family family : families.values()) {
            for (FamilyMember member : findMembers(family.getId())) {
                if (member.getNickname().equals(nickname)) { result.add(family); break; }
            }
        }
        return result;
    }
    public List<Family> findByUserId(Long userId) { List<Family> result = new ArrayList<Family>(); for (Family family : families.values()) if (isMember(family.getId(), userId)) result.add(family); return result; }
    public List<FamilyMember> findMembers(Long id) {
        return new ArrayList<FamilyMember>(members.getOrDefault(id, Collections.<FamilyMember>emptyList()));
    }
    public boolean isMember(Long familyId, Long userId) {
        return findMembers(familyId).stream().anyMatch(member -> member.getUserId().equals(userId));
    }
    public synchronized FamilyMember addMember(Long id, String nickname, String role) {
        long userId = ids.incrementAndGet();
        FamilyMember member = new FamilyMember(memberIds.incrementAndGet(), id, userId, nickname, role);
        members.get(id).add(member);
        return member;
    }
    public synchronized FamilyMember addMemberUserId(Long id, Long userId, String nickname, String role) { FamilyMember member = new FamilyMember(memberIds.incrementAndGet(), id, userId, nickname, role); members.get(id).add(member); return member; }
    public synchronized boolean updateMemberRole(Long id, Long memberId, String role) {
        List<FamilyMember> list = members.get(id); if (list == null) return false;
        for (int i = 0; i < list.size(); i++) {
            FamilyMember member = list.get(i);
            if (member.getId().equals(memberId) && !"OWNER".equals(member.getRole())) {
                list.set(i, new FamilyMember(member.getId(), id, member.getUserId(), member.getNickname(), role));
                return true;
            }
        }
        return false;
    }
    public synchronized boolean removeMember(Long id, Long memberId) {
        List<FamilyMember> list = members.get(id); if (list == null) return false;
        return list.removeIf(member -> member.getId().equals(memberId) && !"OWNER".equals(member.getRole()));
    }
    public synchronized boolean renameFamily(Long id, String name) {
        Family family = families.get(id);
        if (family == null) return false;
        families.put(id, new Family(id, name, family.getOwnerUserId()));
        return true;
    }
    public synchronized boolean deleteFamily(Long id) {
        if (!families.containsKey(id)) return false;
        orderRepository.deleteByFamilyId(id);
        dishRepository.deleteByFamilyId(id);
        menuRepository.deleteByFamilyId(id);
        wheelRepository.deleteByFamilyId(id);
        members.remove(id);
        families.remove(id);
        return true;
    }
}
