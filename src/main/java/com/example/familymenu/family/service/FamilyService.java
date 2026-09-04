package com.example.familymenu.family.service;
import com.example.familymenu.family.domain.*; import java.util.*;
public interface FamilyService { Family create(String name,String ownerName); Family get(Long id); List<FamilyMember> members(Long id); FamilyMember addMember(Long id,String nickname,String role); void removeMember(Long id,Long memberId); }
