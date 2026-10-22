package com.example.familymenu.family.api;
import com.example.familymenu.common.api.ApiResponse; import com.example.familymenu.family.domain.*; import com.example.familymenu.family.dto.*; import com.example.familymenu.family.service.FamilyService; import javax.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.web.bind.annotation.*; import java.util.*;
import com.example.familymenu.auth.api.LoginRequired; import com.example.familymenu.auth.api.CurrentUser;
@RestController @RequestMapping("/api/families") @RequiredArgsConstructor @LoginRequired public class FamilyController { private final FamilyService service;
 @PostMapping public ApiResponse<Family> create(@Valid @RequestBody CreateFamilyRequest r){return ApiResponse.success(service.createForUser(r.getName(),CurrentUser.requireId()));}
 @GetMapping("/{id}") public ApiResponse<Family> get(@PathVariable Long id){return ApiResponse.success(service.get(id));}
 @GetMapping public ApiResponse<List<Family>> mine(){return ApiResponse.success(service.mineByUserId(CurrentUser.requireId()));}
 @GetMapping("/{id}/members") public ApiResponse<List<FamilyMember>> members(@PathVariable Long id){return ApiResponse.success(service.members(id));}
 @PutMapping("/{id}") public ApiResponse<Family> update(@PathVariable Long id,@Valid @RequestBody UpdateFamilyRequest r){return ApiResponse.success(service.rename(id,r.getName(),CurrentUser.requireId()));}
 @PatchMapping("/{id}/members/{memberId}/role") public ApiResponse<FamilyMember> role(@PathVariable Long id,@PathVariable Long memberId,@Valid @RequestBody UpdateMemberRoleRequest r){return ApiResponse.success(service.updateMemberRole(id,memberId,r.getRole(),CurrentUser.requireId()));}
 @DeleteMapping("/{id}/members/{memberId}") public ApiResponse<Void> remove(@PathVariable Long id,@PathVariable Long memberId){service.removeMember(id,memberId,CurrentUser.requireId());return ApiResponse.success(null);}
 @DeleteMapping("/{id}") public ApiResponse<Void> delete(@PathVariable Long id){service.delete(id,CurrentUser.requireId());return ApiResponse.success(null);}
}
