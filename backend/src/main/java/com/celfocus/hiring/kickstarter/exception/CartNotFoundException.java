package com.celfocus.hiring.kickstarter.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CartNotFoundException forUser(String username) {
        return new CartNotFoundException("Cart not found for user: " + username);
    }
}
