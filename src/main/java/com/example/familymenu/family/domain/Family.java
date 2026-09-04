package com.example.familymenu.family.domain;

public class Family {
    private final Long id;
    private final String name;
    private final Long ownerUserId;
    public Family(Long id, String name, Long ownerUserId) { this.id = id; this.name = name; this.ownerUserId = ownerUserId; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getOwnerUserId() { return ownerUserId; }
}
