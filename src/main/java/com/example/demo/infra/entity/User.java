package com.example.demo.infra.entity;

import lombok.Getter;

@Getter
public class User {

    private Long id;

    public static User asNew(Long id) {
        User user = new User();
        user.id = id;
        return user;
    }
}
