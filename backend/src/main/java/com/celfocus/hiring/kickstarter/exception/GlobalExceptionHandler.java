package com.celfocus.hiring.kickstarter.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ItemNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionErrorDTO handleItemNotFoundException(ItemNotFoundException ex) {
        log.warn("Item not found Exception Thrown: {}", ex.getMessage());
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
        log.warn("Insufficient stock Exception Thrown: {}", ex.getMessage());
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
        log.warn("Cart not found Exception Thrown: {}", ex.getMessage());
        return new ExceptionErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException and returns a 400 Bad Request response.
     *
     * @param ex the exception to handle
     * @return a response entity with the error details
     */

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionErrorDTO handleAllOtherExceptions(Exception ex) {
        // ERROR because we didn’t anticipate it—could be a bug or infrastructure failure
        log.error("Unhandled exception caught at top level", ex);
        return new ExceptionErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later."
        );
    }


}
