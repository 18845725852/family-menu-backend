package com.example.familymenu.family.api;
import com.example.familymenu.common.api.ApiResponse; import com.example.familymenu.family.domain.*; import com.example.familymenu.family.dto.*; import com.example.familymenu.family.service.FamilyService; import javax.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/families") @RequiredArgsConstructor public class FamilyController { private final FamilyService service;
 @PostMapping public ApiResponse<Family> create(@Valid @RequestBody CreateFamilyRequest r){return ApiResponse.success(service.create(r.getName(),r.getOwnerName()));}
 @GetMapping("/{id}") public ApiResponse<Family> get(@PathVariable Long id){return ApiResponse.success(service.get(id));}
 @GetMapping public ApiResponse<List<Family>> mine(@RequestParam String nickname){return ApiResponse.success(service.mine(nickname));}
 @GetMapping("/{id}/members") public ApiResponse<List<FamilyMember>> members(@PathVariable Long id){return ApiResponse.success(service.members(id));}
 @PostMapping("/{id}/members") public ApiResponse<FamilyMember> add(@PathVariable Long id,@Valid @RequestBody AddMemberRequest r){return ApiResponse.success(service.addMember(id,r.getNickname(),r.getRole()));}
 @PatchMapping("/{id}/members/{memberId}/role") public ApiResponse<FamilyMember> role(@PathVariable Long id,@PathVariable Long memberId,@Valid @RequestBody UpdateMemberRoleRequest r){return ApiResponse.success(service.updateMemberRole(id,memberId,r.getRole()));}
 @DeleteMapping("/{id}/members/{memberId}") public ApiResponse<Void> remove(@PathVariable Long id,@PathVariable Long memberId){service.removeMember(id,memberId);return ApiResponse.success(null);}
 @DeleteMapping("/{id}") public ApiResponse<Void> delete(@PathVariable Long id){service.delete(id);return ApiResponse.success(null);}
}
