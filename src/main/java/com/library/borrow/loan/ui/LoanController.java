package com.library.borrow.loan.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.borrow.loan.application.LoanDto;
import com.library.borrow.loan.application.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
class LoanController {

	private final LoanService loanService;

    @PostMapping("/borrow/loans")
    ResponseEntity<LoanDto> holdBook(@RequestBody HoldRequest request) {
        var loanDto = loanService.hold(request.barcode(), request.patronId());
        return ResponseEntity.ok(loanDto);
    }

    @PostMapping("/borrow/loans/{id}/checkout")
    ResponseEntity<LoanDto> checkoutBook(@PathVariable("id") Long loanId) {
        var loanDto = loanService.checkout(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @PostMapping("/borrow/loans/{id}/checkin")
    ResponseEntity<LoanDto> checkinBook(@PathVariable("id") Long loanId) {
        var loanDto = loanService.checkin(loanId);
        return ResponseEntity.ok(loanDto);
    }

    @GetMapping("/borrow/loans/{id}")
    ResponseEntity<LoanDto> viewSingleLoan(@PathVariable("id") Long loanId) {
        return loanService.locate(loanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/borrow/loans")
    ResponseEntity<List<LoanDto>> viewActiveLoans(@RequestParam String type) {
        if ("onhold".equalsIgnoreCase(type)) {
            return ResponseEntity.ok(loanService.onHoldLoans());
        }
        return ResponseEntity.ok(loanService.activeLoans());
    }

    record HoldRequest(String barcode, Long patronId) {
    }
}
