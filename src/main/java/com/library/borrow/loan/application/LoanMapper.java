package com.library.borrow.loan.application;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.library.borrow.book.domain.Book;
import com.library.borrow.loan.domain.Loan;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface LoanMapper {

	 @Mapping(source = "bookBarcode", target = "bookBarcode", qualifiedByName = "normalizeBarcode")
	    LoanDto toDto(Loan loan);

	    Loan toEntity(LoanDto loanDto);

	    @Named("normalizeBarcode")
	    static String normalizeBarcode(Book.Barcode barcode) {
	        return barcode.barcode();
	    }
}
