package com.mysite.sbb.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STUDENT,
    TEACHER,
    ADMIN;

}
