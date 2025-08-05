package com.celfocus.hiring.kickstarter.api;

import com.celfocus.hiring.kickstarter.api.dto.CartItemInput;
import com.celfocus.hiring.kickstarter.db.entity.CartEntity;
import com.celfocus.hiring.kickstarter.db.entity.CartItemEntity;
import com.celfocus.hiring.kickstarter.db.entity.CartItemPK;
import com.celfocus.hiring.kickstarter.db.repo.CartItemRepository;
import com.celfocus.hiring.kickstarter.db.repo.CartRepository;
import com.celfocus.hiring.kickstarter.db.repo.ProductRepository;
import com.celfocus.hiring.kickstarter.domain.Cart;
import com.celfocus.hiring.kickstarter.domain.CartItem;
import com.celfocus.hiring.kickstarter.exception.CartNotFoundException;
import com.celfocus.hiring.kickstarter.exception.InsufficientStockException;
import com.celfocus.hiring.kickstarter.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public void addItemToCart(String username, CartItemInput itemInput) {
        var cart = cartRepository.findByUserId(username).orElseGet(() -> {
            var newCart = new CartEntity();
            newCart.setUserId(username);
            return cartRepository.save(newCart);
        });

        cartItemRepository.findById(new CartItemPK(itemInput.itemId(), cart.getId()))
                .ifPresentOrElse((item) -> updateItemQuantity(item, 1), () -> {
                    addNewItemToCart(itemInput, cart);
                });
    }

    private void addNewItemToCart(CartItemInput itemInput, CartEntity cart) {
        var product = productRepository.findBySku(itemInput.itemId())
                .orElseThrow(() -> ItemNotFoundException.forItemId(itemInput.itemId()));

        int requestedQuantity = 1; // When adding a new item, quantity is 1
        int availableStock = product.getQuantity();

        if (availableStock < requestedQuantity) {
            throw new InsufficientStockException(itemInput.itemId(), requestedQuantity, availableStock);
        }

        var cartItem = new CartItemEntity();
        cartItem.setQuantity(1);
        cartItem.setItemId(itemInput.itemId());
        cartItem.setCartId(cart.getId());
        cartItem.setCart(cart);
        cartItem.setPrice(product.getPrice());
        cartItemRepository.save(cartItem);

        product.setQuantity(availableStock - requestedQuantity);
        productRepository.save(product);
    }

    private void updateItemQuantity(CartItemEntity item, int byCount) {
        setItemQuantity(item, item.getQuantity() + byCount);
    }

    private void setItemQuantity(CartItemEntity item, int quantity) {

        int oldQuantity = item.getQuantity();
        int quantityChange = quantity - oldQuantity;

        var product = productRepository.findBySku(item.getItemId())
                .orElseThrow(() -> ItemNotFoundException.forItemId(item.getItemId()));

        int availableStock = product.getQuantity();

        if (availableStock < quantityChange) {
            throw new InsufficientStockException(item.getItemId(), quantityChange, availableStock);
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        product.setQuantity(product.getQuantity() - quantityChange);
        productRepository.save(product);
    }

    public void clearCart(String username) {
        cartRepository.findByUserId(username).ifPresentOrElse(
            cart -> {
                cart.getItems().forEach(cartItem -> {
                    productRepository.findBySku(cartItem.getItemId()).ifPresent(product -> {
                        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                        productRepository.save(product);
                    });
                });
                cartRepository.deleteByUserId(username);
            },
            () -> {
                throw CartNotFoundException.forUser(username);
            }
        );
    }

    public Cart<? extends CartItem> getCart(String username) {
        return cartRepository.findByUserId(username)
                .map(this::mapToCart)
                .orElseThrow(() -> CartNotFoundException.forUser(username));
    }

    public void removeItemFromCart(String username, String itemId) {
        cartRepository.findByUserId(username)
                .ifPresentOrElse(
                    cart -> {
                        Optional<CartItemEntity> cartItemOptional = cartItemRepository.findById(new CartItemPK(itemId, cart.getId()));
                        if (cartItemOptional.isPresent()) {
                            CartItemEntity cartItem = cartItemOptional.get();
                            cartItemRepository.deleteById(new CartItemPK(itemId, cart.getId()));

                            // Return quantity to product stock
                            productRepository.findBySku(itemId).ifPresent(product -> {
                                product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                                productRepository.save(product);
                            });
                        }
                        else {
                            throw ItemNotFoundException.forItemId(itemId);
                        }
                    },
                    () -> {
                        throw CartNotFoundException.forUser(username);
                    }
                );
    }

    private Cart<? extends CartItem> mapToCart(CartEntity cartEntity) {
        Cart<CartItemEntity> cart = new Cart<>();
        cart.setUserId(cartEntity.getUserId());
        cart.setItems(cartEntity.getItems());
        return cart;
    }
}
