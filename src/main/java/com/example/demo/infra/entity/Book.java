package com.example.demo.infra.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Book {
    private boolean sale;
    private int minAge;

    public boolean onSale() {
        return sale;
    }
}
