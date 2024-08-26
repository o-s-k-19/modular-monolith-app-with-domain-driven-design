package com.library.catalog.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.catalog.application.BookDto;
import com.library.catalog.application.CatalogService;
import com.library.catalog.domain.CatalogBook.Barcode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CatalogController {

	private final CatalogService catalogService;

    @PostMapping("/catalog/books")
    ResponseEntity<BookDto> addBookToInventory(@RequestBody AddBookRequest request) {
        var bookDto = catalogService.addToCatalog(request.title(), new Barcode(request.catalogNumber()), request.isbn(), request.author());
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/catalog/books/{id}")
    ResponseEntity<BookDto> viewSingleBook(@PathVariable("id") Long id) {
        return catalogService.locate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/catalog/books")
    ResponseEntity<List<BookDto>> viewBooks() {
        return ResponseEntity.ok(catalogService.fetchBooks());
    }

    record AddBookRequest(String title, String catalogNumber,
                          String isbn, String author) {
    }
    
}
