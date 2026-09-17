package com.example.familymenu.family.service;

import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.dto.InviteCodeResponse;
import com.example.familymenu.family.dto.InvitePreviewResponse;
import com.example.familymenu.family.repository.FamilyRepository;
import com.example.familymenu.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Profile("!memory")
@RequiredArgsConstructor
public class FamilyInvitationService {
    private final JdbcTemplate jdbc;
    private final FamilyRepository familyRepository;
    private final SecureRandom random = new SecureRandom();

    public InviteCodeResponse create(Long familyId, Long userId) {
        requireMember(familyId, userId);
        String code;
        do { code = randomCode(); } while (!jdbc.queryForList("SELECT id FROM family_invitations WHERE invite_code=?", Long.class, code).isEmpty());
        LocalDateTime expires = LocalDateTime.now().plusDays(7);
        jdbc.update("INSERT INTO family_invitations(family_id,invite_code,expires_at,created_by) VALUES(?,?,?,?)", familyId, code, expires, userId);
        return new InviteCodeResponse(familyId, code, expires.toString());
    }

    public InvitePreviewResponse preview(String code) {
        Long familyId = findFamilyIdByCode(code);
        Family family = familyRepository.findById(familyId).orElseThrow(() -> new BusinessException("家庭不存在"));
        return new InvitePreviewResponse(family.getId(), family.getName(), code.trim().toUpperCase());
    }

    public Family join(String code, Long userId) {
        Long familyId = findFamilyIdByCode(code);
        if (!familyRepository.isMember(familyId, userId)) {
            String nickname = jdbc.queryForObject("SELECT nickname FROM users WHERE id=?", String.class, userId);
            familyRepository.addMemberUserId(familyId, userId, nickname, "MEMBER");
        }
        return familyRepository.findById(familyId).orElseThrow(() -> new BusinessException("家庭不存在"));
    }

    private Long findFamilyIdByCode(String code) {
        List<Long> familyIds = jdbc.query("SELECT family_id FROM family_invitations WHERE invite_code=? AND expires_at>CURRENT_TIMESTAMP", new Object[]{code.trim().toUpperCase()}, (rs, n) -> rs.getLong(1));
        if (familyIds.isEmpty()) throw new BusinessException("邀请码无效或已过期");
        return familyIds.get(0);
    }

    private void requireMember(Long familyId, Long userId) { if (!familyRepository.isMember(familyId, userId)) throw new BusinessException("你不是该家庭成员"); }
    private String randomCode() { final char[] chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray(); StringBuilder b = new StringBuilder(6); for (int i=0;i<6;i++) b.append(chars[random.nextInt(chars.length)]); return b.toString(); }
}
