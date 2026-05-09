package com.college.management.repository;

import com.college.management.entity.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    List<LibraryBook> findByTitleContainingIgnoreCase(String title);
    List<LibraryBook> findByCategory(String category);
    List<LibraryBook> findByAuthorContainingIgnoreCase(String author);
    Optional<LibraryBook> findByIsbn(String isbn);
    List<LibraryBook> findByAvailableCopiesGreaterThan(Integer count);
}
