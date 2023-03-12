package com.example.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.library.models.Book;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Integer> {

    public List<Book> findByTitleStartingWith(String s);

}
