package com.library.borrow.loan.application;

import java.time.LocalDate;

import com.library.borrow.loan.domain.Loan;

public record LoanDto(Long id, String bookBarcode, Long patronId, LocalDate dateOfHold,
        LocalDate dateOfCheckout, int holdDurationInDays, int loanDurationInDays,
        LocalDate dateOfCheckin, Loan.LoanStatus status) {
}
