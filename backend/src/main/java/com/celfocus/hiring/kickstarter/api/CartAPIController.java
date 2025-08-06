package com.celfocus.hiring.kickstarter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.celfocus.hiring.kickstarter.api.dto.CartItemInput;
import com.celfocus.hiring.kickstarter.api.dto.CartItemResponse;
import com.celfocus.hiring.kickstarter.api.dto.CartResponse;
import com.celfocus.hiring.kickstarter.domain.Cart;
import com.celfocus.hiring.kickstarter.domain.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(CartAPIController.CARTS_PATH)
public class CartAPIController implements CartAPI {

    static final String CARTS_PATH = "/api/v1/carts";

    private static final Logger log = LoggerFactory.getLogger(CartAPIController.class);

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartAPIController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index() {
        log.info("GET {}/ called", CARTS_PATH);
        return "Greetings from Celfocus!";
    }

    @Override
    public ResponseEntity<Void> addItemToCart(String username, CartItemInput itemInput) {
        log.info("POST {}/items called for user={}", CARTS_PATH, username);
        cartService.addItemToCart(username, itemInput);
        log.info("POST {}/items completed for user={} status=201", CARTS_PATH, username);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> clearCart(String username) {
        log.info("DELETE {} called for user={}", CARTS_PATH, username);
        cartService.clearCart(username);
        log.info("DELETE {} completed for user={} status=204", CARTS_PATH, username);
        return ResponseEntity.status(204).build();
    }

    @Override
    public ResponseEntity<CartResponse> getCart(String username) {
        log.info("GET {} called for user={}", CARTS_PATH, username);
        var cart = cartService.getCart(username);
        var response = mapToCartResponse(cart);
        log.info("GET {} completed for user={} returning {} items", CARTS_PATH, username, response.items().size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> removeItemFromCart(String username, String itemId) {
        log.info("DELETE {}/items/{} called for user={}", CARTS_PATH, itemId, username);
        cartService.removeItemFromCart(username, itemId);
        log.info("DELETE {}/items/{} completed for user={} status=204", CARTS_PATH, itemId, username);
        return ResponseEntity.status(204).build();
    }

    private CartResponse mapToCartResponse(Cart<? extends CartItem> cart) {
        return new CartResponse(cart.getItems().stream().map(this::mapToCartItemResponse).collect(Collectors.toList()));
    }

    private CartItemResponse mapToCartItemResponse(CartItem item) {
        var product = productService.getProduct(item.getItemId());
        return new CartItemResponse(item.getItemId(), item.getQuantity(), product.orElseThrow().getPrice(), product.orElseThrow().getName());
    }
}
