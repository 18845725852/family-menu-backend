package com.example.familymenu.family.api;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.common.exception.BusinessException;
import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.dto.InviteCodeResponse;
import com.example.familymenu.family.dto.JoinFamilyRequest;
import com.example.familymenu.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@LoginRequired
@Profile("memory")
public class MemoryFamilyInvitationController {
    private final FamilyRepository familyRepository;
    private final Map<String, Long> codes = new ConcurrentHashMap<String, Long>();

    @PostMapping("/families/{familyId}/invite-code")
    public ApiResponse<InviteCodeResponse> create(@PathVariable Long familyId) {
        Long userId = CurrentUser.requireId();
        if (!familyRepository.isMember(familyId, userId)) throw new BusinessException("你不是该家庭成员");
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        codes.put(code, familyId);
        return ApiResponse.success(new InviteCodeResponse(familyId, code, LocalDateTime.now().plusDays(7).toString()));
    }

    @PostMapping("/family-invitations/join")
    public ApiResponse<Family> join(@Valid @RequestBody JoinFamilyRequest request) {
        Long familyId = codes.get(request.getInviteCode().trim().toUpperCase());
        if (familyId == null) throw new BusinessException("邀请码无效或已过期");
        Long userId = CurrentUser.requireId();
        if (!familyRepository.isMember(familyId, userId)) {
            familyRepository.addMemberUserId(familyId, userId, "微信用户", "MEMBER");
        }
        return ApiResponse.success(familyRepository.findById(familyId).orElseThrow(() -> new BusinessException("家庭不存在")));
    }
}
