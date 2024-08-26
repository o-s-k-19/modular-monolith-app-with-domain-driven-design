package com.library.borrow.book.domain;

import java.time.LocalDate;

public record BookReturnedEvent(Long bookId,
        String isbn,
        String inventoryNumber,
        Long patronId,
        LocalDate dateOfReturn) {

}
