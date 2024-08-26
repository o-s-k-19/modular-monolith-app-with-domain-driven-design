package com.library.borrow.loan.application;

import java.util.List;
import java.util.Optional;

public interface LoanService {

	 LoanDto hold(String barcode, Long patronId);
	 LoanDto checkout(Long loanId);
	 LoanDto checkin(Long loanId);
	 List<LoanDto> activeLoans();
	 List<LoanDto> onHoldLoans();
	 Optional<LoanDto> locate(Long loanId);
}
