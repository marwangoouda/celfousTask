package com.celfocus.hiring.kickstarter.api;

import com.celfocus.hiring.kickstarter.api.dto.CartItemInput;
import com.celfocus.hiring.kickstarter.api.dto.CartResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
public interface CartAPI {
    @PostMapping("/items")
    ResponseEntity<Void> addItemToCart(@RequestHeader("username") String username, @Valid @RequestBody CartItemInput itemInput);

    @DeleteMapping
    ResponseEntity<Void> clearCart(@RequestHeader("username") String username);

    @GetMapping
    ResponseEntity<CartResponse> getCart(@RequestHeader("username") String username);

    @DeleteMapping("/items/{itemId}")
    ResponseEntity<Void> removeItemFromCart(@RequestHeader("username") String username, @PathVariable("itemId") String itemId);
}
