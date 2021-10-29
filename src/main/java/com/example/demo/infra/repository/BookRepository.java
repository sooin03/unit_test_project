package com.example.demo.infra.repository;


import com.example.demo.infra.entity.Book;

import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long bookId);
}
