package com.library.borrow.loan.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.borrow.loan.domain.Loan.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findLoanByStatus(LoanStatus status);
}
