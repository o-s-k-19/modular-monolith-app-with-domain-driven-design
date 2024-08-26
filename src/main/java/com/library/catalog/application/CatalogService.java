package com.library.catalog.application;

import java.util.List;
import java.util.Optional;

import com.library.catalog.domain.CatalogBook.Barcode;

public interface CatalogService {

	BookDto addToCatalog(String title, Barcode catalogNumber, String isbn, String authorName);
	Optional<BookDto> locate(Long id);
	List<BookDto> fetchBooks();
}
