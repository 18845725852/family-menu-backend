package com.example.familymenu.family.service;
import com.example.familymenu.family.domain.*; import java.util.*;
public interface FamilyService { Family create(String name,String ownerName); Family createForUser(String name,Long userId,String ownerName); Family get(Long id); List<Family> mine(String nickname); List<Family> mineByUserId(Long userId); List<FamilyMember> members(Long id); FamilyMember addMember(Long id,String nickname,String role); FamilyMember updateMemberRole(Long id,Long memberId,String role); void removeMember(Long id,Long memberId); void delete(Long id); }
