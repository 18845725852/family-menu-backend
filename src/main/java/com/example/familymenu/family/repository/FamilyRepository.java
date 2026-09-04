package com.example.familymenu.family.repository;
import com.example.familymenu.family.domain.*; import java.util.*;
public interface FamilyRepository { Family create(String name, String ownerName); Optional<Family> findById(Long id); List<Family> findByUserNickname(String nickname); List<FamilyMember> findMembers(Long familyId); FamilyMember addMember(Long familyId, String nickname, String role); boolean updateMemberRole(Long familyId, Long memberId, String role); boolean removeMember(Long familyId, Long memberId); boolean deleteFamily(Long familyId); }
