package com.ll.medium.member.role;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    private String role;

    MemberRole(String role) {
        this.role = role;
    }
}
