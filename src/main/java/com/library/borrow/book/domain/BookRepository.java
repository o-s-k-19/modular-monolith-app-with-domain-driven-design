package com.library.borrow.book.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.borrow.book.domain.Book.Barcode;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByInventoryNumber(Barcode inventoryNumber);

}
