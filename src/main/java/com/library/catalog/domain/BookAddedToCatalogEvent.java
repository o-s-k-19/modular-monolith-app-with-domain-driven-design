package com.library.catalog.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record BookAddedToCatalogEvent(String title, String inventoryNumber,
        String isbn, String author) {

}
