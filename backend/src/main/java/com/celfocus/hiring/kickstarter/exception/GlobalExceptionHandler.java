package com.celfocus.hiring.kickstarter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ItemNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionErrorDTO handleItemNotFoundException(ItemNotFoundException ex) {
        return new ExceptionErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * Handles InsufficientStockException and returns a 400 Bad Request response.
     *
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionErrorDTO handleInsufficientStockException(InsufficientStockException ex) {
        return new ExceptionErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * Handles CartNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionErrorDTO handleCartNotFoundException(CartNotFoundException ex) {
        return new ExceptionErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }


}
