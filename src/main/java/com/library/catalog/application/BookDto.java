package com.library.catalog.application;

import com.library.catalog.domain.CatalogBook;

public record BookDto(Long id, String title, CatalogBook.Barcode catalogNumber,
        String isbn, CatalogBook.Author author) {
}
