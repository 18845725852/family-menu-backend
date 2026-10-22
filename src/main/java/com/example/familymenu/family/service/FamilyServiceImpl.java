package com.example.familymenu.family.service;

import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.domain.FamilyMember;
import com.example.familymenu.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {
    private final FamilyRepository repo;

    public Family createForUser(String name, Long userId) { return repo.createForUser(name, userId); }
    public Family get(Long id) {
        return repo.findById(id).orElseThrow(() -> new BusinessException("家庭不存在: " + id));
    }
    public List<Family> mine(String nickname) { return repo.findByUserNickname(nickname); }
    public List<Family> mineByUserId(Long userId) { return repo.findByUserId(userId); }
    public List<FamilyMember> members(Long id) { get(id); return repo.findMembers(id); }

    private String role(String value) {
        String role = value == null || value.trim().isEmpty() ? "MEMBER" : value.toUpperCase();
        if (!Arrays.asList("OWNER", "ADMIN", "MEMBER").contains(role)) {
            throw new BusinessException("成员角色不合法");
        }
        return role;
    }

    public FamilyMember addMember(Long id, String nickname, String value) {
        get(id);
        String role = role(value);
        for (FamilyMember member : repo.findMembers(id)) {
            if (member.getNickname().equals(nickname)) throw new BusinessException("该成员已在家庭中");
        }
        return repo.addMember(id, nickname, role);
    }

    private Family requireOwner(Long id, Long userId) {
        Family family = get(id);
        if (!family.getOwnerUserId().equals(userId)) throw new BusinessException("仅家庭组创建者可以操作");
        return family;
    }

    public Family rename(Long id, String name, Long userId) {
        Family family = requireOwner(id, userId);
        String cleaned = name == null ? "" : name.trim();
        if (cleaned.isEmpty()) throw new BusinessException("请填写家庭组名称");
        if (cleaned.length() > 30) throw new BusinessException("家庭组名称不能超过30个字");
        if (cleaned.equals(family.getName())) return family;
        if (!repo.renameFamily(id, cleaned)) throw new BusinessException("家庭组不存在或名称保存失败");
        return get(id);
    }

    public FamilyMember updateMemberRole(Long id, Long memberId, String value, Long userId) {
        requireOwner(id, userId);
        String role = role(value);
        if ("OWNER".equals(role)) throw new BusinessException("不能将成员设置为拥有者");
        if (!repo.updateMemberRole(id, memberId, role)) throw new BusinessException("成员不存在或不能修改拥有者");
        return repo.findMembers(id).stream().filter(member -> member.getId().equals(memberId)).findFirst().get();
    }

    public void removeMember(Long id, Long memberId, Long userId) {
        Family family = get(id);
        FamilyMember target = repo.findMembers(id).stream().filter(member -> member.getId().equals(memberId))
                .findFirst().orElseThrow(() -> new BusinessException("成员不存在"));
        if (family.getOwnerUserId().equals(target.getUserId()) || "OWNER".equals(target.getRole())) {
            throw new BusinessException("创建者不能退出家庭组，请使用删除家庭组");
        }
        if (!family.getOwnerUserId().equals(userId) && !target.getUserId().equals(userId)) {
            throw new BusinessException("只能退出自己的家庭组，不能移除其他成员");
        }
        if (!repo.removeMember(id, memberId)) throw new BusinessException("成员不存在或不能移除拥有者");
    }

    public void delete(Long id, Long userId) {
        requireOwner(id, userId);
        if (!repo.deleteFamily(id)) throw new BusinessException("家庭删除失败");
    }
}
