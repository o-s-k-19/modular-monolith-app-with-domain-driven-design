package com.library.catalog.application;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.catalog.domain.BookAddedToCatalogEvent;
import com.library.catalog.domain.CatalogBook;
import com.library.catalog.domain.CatalogBook.Barcode;
import com.library.catalog.domain.CatalogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

	private final CatalogRepository catalogRepository;
	private final BookMapper bookMapper;
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public BookDto addToCatalog(String title, Barcode catalogNumber, String isbn, String authorName) {
		var book = new CatalogBook(title, catalogNumber, isbn, new CatalogBook.Author(authorName));
		var dto = bookMapper.toDto(catalogRepository.save(book));
		applicationEventPublisher.publishEvent(new BookAddedToCatalogEvent(dto.title(), dto.catalogNumber().barcode(), dto.isbn(), dto.author().name()));
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<BookDto> locate(Long id) {
		return catalogRepository.findById(id).map(bookMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookDto> fetchBooks() {
		return catalogRepository.findAll().stream().map(bookMapper::toDto).toList();
	}

}
