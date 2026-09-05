package com.example.familymenu.family.api;

import com.example.familymenu.auth.api.CurrentUser;
import com.example.familymenu.auth.api.LoginRequired;
import com.example.familymenu.common.api.ApiResponse;
import com.example.familymenu.family.domain.Family;
import com.example.familymenu.family.dto.InviteCodeResponse;
import com.example.familymenu.family.dto.JoinFamilyRequest;
import com.example.familymenu.family.service.FamilyInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@LoginRequired
@Profile("!memory")
public class FamilyInvitationController {
    private final FamilyInvitationService service;

    @PostMapping("/families/{familyId}/invite-code")
    public ApiResponse<InviteCodeResponse> create(@PathVariable Long familyId) {
        return ApiResponse.success(service.create(familyId, CurrentUser.requireId()));
    }

    @PostMapping("/family-invitations/join")
    public ApiResponse<Family> join(@Valid @RequestBody JoinFamilyRequest request) {
        return ApiResponse.success(service.join(request.getInviteCode(), CurrentUser.requireId()));
    }
}
