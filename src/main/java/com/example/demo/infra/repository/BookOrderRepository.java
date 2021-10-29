package com.example.demo.infra.repository;


import com.example.demo.infra.entity.BookOrder;

public interface BookOrderRepository {
    BookOrder save(BookOrder bookOrder);
}
