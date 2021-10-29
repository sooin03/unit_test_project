package com.example.demo.infra.repository;


import com.example.demo.infra.entity.BookStock;

import java.util.Optional;

public interface BookStockRepository {
    Optional<BookStock> findByBookId(long bookId);

    void save(BookStock bookStock);
}
