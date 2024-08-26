package com.library.borrow.book.domain;

import java.time.LocalDate;

public record BookPlacedOnHoldEvent(Long bookId,
        String isbn,
        String inventoryNumber,
        Long patronId,
        LocalDate dateOfHold) {

}
