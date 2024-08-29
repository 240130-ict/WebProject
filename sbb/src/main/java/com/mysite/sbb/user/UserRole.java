package com.mysite.sbb.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
