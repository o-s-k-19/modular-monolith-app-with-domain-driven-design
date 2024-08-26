package com.library.borrow.book.application;

import com.library.borrow.book.domain.BookCollectedEvent;
import com.library.borrow.book.domain.BookPlacedOnHoldEvent;
import com.library.borrow.book.domain.BookReturnedEvent;
import com.library.catalog.domain.BookAddedToCatalogEvent;

public interface BookService {

    void on(BookAddedToCatalogEvent event);
    void on(BookPlacedOnHoldEvent event);
    void on(BookCollectedEvent event);
    void on(BookReturnedEvent event);
}
