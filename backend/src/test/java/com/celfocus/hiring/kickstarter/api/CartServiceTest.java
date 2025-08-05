package com.celfocus.hiring.kickstarter.api;

import com.celfocus.hiring.kickstarter.api.dto.CartItemInput;
import com.celfocus.hiring.kickstarter.db.entity.CartEntity;
import com.celfocus.hiring.kickstarter.db.entity.CartItemEntity;
import com.celfocus.hiring.kickstarter.db.entity.CartItemPK;
import com.celfocus.hiring.kickstarter.db.entity.ProductEntity;
import com.celfocus.hiring.kickstarter.db.repo.CartItemRepository;
import com.celfocus.hiring.kickstarter.db.repo.CartRepository;
import com.celfocus.hiring.kickstarter.db.repo.ProductRepository;
import com.celfocus.hiring.kickstarter.exception.CartNotFoundException;
import com.celfocus.hiring.kickstarter.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private CartEntity cartEntity;
    private ProductEntity productEntity;
    private CartItemInput cartItemInput;

    @BeforeEach
    void setUp() {
        cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setUserId("testuser");
        cartEntity.setItems(new ArrayList<>());

        productEntity = new ProductEntity();
        productEntity.setSku("TEST-SKU");
        productEntity.setName("Test Product");
        productEntity.setPrice(BigDecimal.valueOf(7));
        productEntity.setQuantity(12);

        cartItemInput = new CartItemInput("TEST-SKU");
    }

    @Test
    void addItemToCart_NewCart_ShouldCreateCartAndAddItem() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.empty());
        when(productRepository.findBySku("TEST-SKU")).thenReturn(Optional.of(productEntity));
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);

        cartService.addItemToCart("testuser", cartItemInput);

        verify(cartRepository).save(any(CartEntity.class));
        verify(cartItemRepository).save(any(CartItemEntity.class));
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void addItemToCart_ExistingCart_ShouldAddItemToExistingCart() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.of(cartEntity));
        when(cartItemRepository.findById(any(CartItemPK.class))).thenReturn(Optional.empty());
        when(productRepository.findBySku("TEST-SKU")).thenReturn(Optional.of(productEntity));

        cartService.addItemToCart("testuser", cartItemInput);

        verify(cartItemRepository).save(any(CartItemEntity.class));
    }

    @Test
    void addItemToCart_ProductNotFound_ShouldThrowItemNotFoundException() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.of(cartEntity));
        when(cartItemRepository.findById(any(CartItemPK.class))).thenReturn(Optional.empty());
        when(productRepository.findBySku("TEST-SKU")).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                cartService.addItemToCart("testuser", cartItemInput));
    }

    @Test
    void getCart_ExistingCart_ShouldReturnCart() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.of(cartEntity));

        var result = cartService.getCart("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUserId());
    }

    @Test
    void getCart_NonExistingCart_ShouldThrowCartNotFoundException() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.getCart("testuser"));
    }

    @Test
    void clearCart_ShouldDeleteCart() {
        CartItemEntity item1 = new CartItemEntity();
        item1.setItemId("TEST-SKU");
        item1.setQuantity(5);
        cartEntity.getItems().add(item1);

        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.of(cartEntity));
        when(productRepository.findBySku("TEST-SKU")).thenReturn(Optional.of(productEntity));

        cartService.clearCart("testuser");

        verify(cartRepository).deleteByUserId("testuser");
        verify(productRepository).save(productEntity);
    }

    @Test
    void clearCart_NonExistingCart_ShouldThrowCartNotFoundException() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.clearCart("testuser"));
    }

    @Test
    void removeItemFromCart_ExistingCart_ShouldRemoveItem() {
        CartItemEntity itemToRemove = new CartItemEntity();
        itemToRemove.setItemId("TEST-SKU");
        itemToRemove.setQuantity(2);
        itemToRemove.setCartId(cartEntity.getId());

        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.of(cartEntity));
        when(cartItemRepository.findById(any(CartItemPK.class))).thenReturn(Optional.of(itemToRemove));
        when(productRepository.findBySku("TEST-SKU")).thenReturn(Optional.of(productEntity));

        cartService.removeItemFromCart("testuser", "TEST-SKU");

        verify(cartItemRepository).deleteById(any(CartItemPK.class));
        verify(productRepository).save(productEntity);
    }

    @Test
    void removeItemFromCart_NonExistingCart_ShouldThrowCartNotFoundException() {
        when(cartRepository.findByUserId("testuser")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.removeItemFromCart("testuser", "TEST-SKU"));
    }
}
