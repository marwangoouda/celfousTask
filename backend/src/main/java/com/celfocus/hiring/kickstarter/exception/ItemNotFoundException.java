package com.celfocus.hiring.kickstarter.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ItemNotFoundException forItemId(String itemId) {
        return new ItemNotFoundException("Item not found with ID: " + itemId);
    }
}
