package com.example.demo.infra.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookOrder {
    private Long id;
    private User user;
    private Book book;

    @Builder
    public BookOrder(Long id, User user, Book book) {
        this.id = id;
        this.user = user;
        this.book = book;
    }

}
