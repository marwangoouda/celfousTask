package com.celfocus.hiring.kickstarter.exception;

public class InvalidCredsException extends RuntimeException {

    public InvalidCredsException(String message) {
        super(message);
    }

    public InvalidCredsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InvalidCredsException forUser(String username) {
        return new InvalidCredsException("Invalid Creds  for user: " + username);
    }
}
