package com.library.borrow.loan.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.borrow.book.domain.Book.Barcode;
import com.library.borrow.book.domain.BookCollectedEvent;
import com.library.borrow.book.domain.BookPlacedOnHoldEvent;
import com.library.borrow.book.domain.BookRepository;
import com.library.borrow.book.domain.BookReturnedEvent;
import com.library.borrow.loan.domain.Loan;
import com.library.borrow.loan.domain.Loan.LoanStatus;
import com.library.borrow.loan.domain.LoanRepository;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {
	
	private final LoanRepository loanRepository;
	private final BookRepository bookRepository;
	private final LoanMapper loanMapper;
	private final ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public LoanDto hold(String barcode, Long patronId) {
		var book = bookRepository.findByInventoryNumber(new Barcode(barcode))
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));
		if (!book.available()) {
			throw new IllegalArgumentException("Book is not available");
		}
		var dateOfHold = LocalDate.now();
		var loan = Loan.of(barcode, dateOfHold, patronId);
		var dto = loanMapper.toDto(loanRepository.save(loan));
		applicationEventPublisher.publishEvent(new BookPlacedOnHoldEvent(patronId, barcode, barcode, patronId, dateOfHold));
		return dto;
	}
	
	@Override
	public LoanDto checkout(Long loanId) {
		var loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new IllegalArgumentException("Loan not found"));
		var book = bookRepository.findByInventoryNumber(loan.getBookBarcode())
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));
		if (!book.onHold()) {
			throw new IllegalArgumentException("Book is not on hold");
		}
		var dateOfCheckout = LocalDate.now();
		loan.activate(dateOfCheckout);
		var dto = loanMapper.toDto(loanRepository.save(loan));
		applicationEventPublisher.publishEvent(
				new BookCollectedEvent(
						book.getId(),
						book.getIsbn(),
						book.getInventoryNumber().barcode(),
						loan.getPatronId(),
						dateOfCheckout)
				);
		return dto;
	}
	@Override
	public LoanDto checkin(Long loanId) {
		var dateOfCheckin = LocalDate.now();
		var loan = loanRepository.findById(loanId)
				.orElseThrow(() -> new IllegalArgumentException("Loan not found"));
		loan.complete(dateOfCheckin);
		var dto = loanMapper.toDto(loanRepository.save(loan));
		var book = bookRepository.findByInventoryNumber(loan.getBookBarcode())
				.orElseThrow(() -> new IllegalArgumentException("Book not found"));
		applicationEventPublisher.publishEvent(new BookReturnedEvent(
				book.getId(), 
				book.getIsbn(), 
				book.getInventoryNumber().barcode(),
				loan.getPatronId(),
				dateOfCheckin
				));
		return dto;
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanDto> activeLoans() {
		return loanRepository.findLoanByStatus(LoanStatus.ACTIVE).stream().map(loanMapper::toDto).toList();
	}
	@Override
	@Transactional(readOnly = true)
	public List<LoanDto> onHoldLoans() {
		return loanRepository.findLoanByStatus(LoanStatus.HOLDING).stream().map(loanMapper::toDto).toList();
	}
	@Override
	@Transactional(readOnly = true)
	public Optional<LoanDto> locate(Long loanId) {
		return loanRepository.findById(loanId).map(loanMapper::toDto);
	}

	

}
