package com.library.catalog.application;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.library.catalog.domain.CatalogBook;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    BookDto toDto(CatalogBook catalogBook);
    CatalogBook toEntity(BookDto bookDto);
}
