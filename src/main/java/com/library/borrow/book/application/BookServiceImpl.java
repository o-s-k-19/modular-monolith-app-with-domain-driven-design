package com.library.borrow.book.application;


import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.borrow.book.domain.Book;
import com.library.borrow.book.domain.Book.Barcode;
import com.library.catalog.domain.BookAddedToCatalogEvent;
import com.library.borrow.book.domain.BookCollectedEvent;
import com.library.borrow.book.domain.BookPlacedOnHoldEvent;
import com.library.borrow.book.domain.BookRepository;
import com.library.borrow.book.domain.BookReturnedEvent;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	private final BookRepository bookRepository;
	
	@Override
	@ApplicationModuleListener
	public void on(BookAddedToCatalogEvent event) {
		bookRepository.save(new Book(event.title(), new Barcode(event.inventoryNumber()), event.isbn()));
	}

	@Override
	@ApplicationModuleListener
	public void on(BookPlacedOnHoldEvent event) {
	var book = bookRepository.findById(event.bookId())
			.map(Book::markOnHold)
			.orElseThrow(() -> new IllegalArgumentException("Book not found"));
	bookRepository.save(book);
	}

	@Override
	@ApplicationModuleListener
	public void on(BookCollectedEvent event) {
		var book = bookRepository.findById(event.bookId())
				.map(Book::markIssued)
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));
		bookRepository.save(book);
	}

	@Override
	@ApplicationModuleListener
	public void on(BookReturnedEvent event) {
		var book = bookRepository.findById(event.bookId())
				.map(Book::markAvailable)
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));
		bookRepository.save(book);
	}

}
