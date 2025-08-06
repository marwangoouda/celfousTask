package com.celfocus.hiring.kickstarter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

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
        log.info("Adding a cartItem for user={}, itemId={}", username, itemInput.itemId());
        var cart = cartRepository.findByUserId(username).orElseGet(() -> {
            log.info("No existing cart for user={}, creating new cart", username);
            var newCart = new CartEntity();
            newCart.setUserId(username);
            CartEntity savedCartEntity = cartRepository.save(newCart);
            log.info("Created cart id={} for user={}", savedCartEntity.getId(), username);
            return savedCartEntity;
        });

        cartItemRepository.findById(new CartItemPK(itemInput.itemId(), cart.getId()))
                .ifPresentOrElse(
                        item -> {
                            log.info("Updating quantity for existing cartItem id={} in cart id={}", item.getItemId(), cart.getId());
                            updateItemQuantity(item, 1);
                        },
                        () -> {
                            log.info("Adding new item with itemId={} to cart id={}", itemInput.itemId(), cart.getId());
                            addNewItemToCart(itemInput, cart);
                        }
                );
    }

    private void addNewItemToCart(CartItemInput itemInput, CartEntity cart) {
        log.debug("addNewItemToCart: fetching product sku={}", itemInput.itemId());
        var product = productRepository.findBySku(itemInput.itemId())
                .orElseThrow(() -> {
                    log.warn("Product {} not found when adding to cart", itemInput.itemId());
                    return ItemNotFoundException.forItemId(itemInput.itemId());
                });

        int requestedQuantity = 1; // When adding a new item, quantity is 1
        int availableStock = product.getQuantity();
        log.debug("Available stock for sku {}: {}", itemInput.itemId(), availableStock);

        if (availableStock < requestedQuantity) {
            log.warn("Insufficient stock for sku={}, requested={}, available={}", itemInput.itemId(), requestedQuantity, availableStock);
            throw new InsufficientStockException(itemInput.itemId(), requestedQuantity, availableStock);
        }

        var cartItem = new CartItemEntity();
        cartItem.setQuantity(1);
        cartItem.setItemId(itemInput.itemId());
        cartItem.setCartId(cart.getId());
        cartItem.setCart(cart);
        cartItem.setPrice(product.getPrice());
        cartItemRepository.save(cartItem);
        log.info("Added cartItem for sku={} to cart id={}", itemInput.itemId(), cart.getId());

        product.setQuantity(availableStock - requestedQuantity);
        productRepository.save(product);
        log.debug("Reduced stock for sku {} to {}", itemInput.itemId(), product.getQuantity());
    }

    private void updateItemQuantity(CartItemEntity item, int byCount) {
        log.debug("updateItemQuantity: cartItem={} currentQty={}, increment={}", item.getItemId(), item.getQuantity(), byCount);
        setItemQuantity(item, item.getQuantity() + byCount);
    }

    private void setItemQuantity(CartItemEntity item, int quantity) {
        int oldQuantity = item.getQuantity();
        int quantityChange = quantity - oldQuantity;
        log.debug("setItemQuantity: itemId={} in cart id={} from {} to {} (change={})",
                item.getItemId(), item.getCartId(), oldQuantity, quantity, quantityChange);

        var product = productRepository.findBySku(item.getItemId())
                .orElseThrow(() -> {
                    log.warn("Product {} not found when updating quantity", item.getItemId());
                    return ItemNotFoundException.forItemId(item.getItemId());
                });

        int availableStock = product.getQuantity();

        if (availableStock < quantityChange) {
            log.warn("Insufficient stock for sku={}, requested change={}, available={}", item.getItemId(), quantityChange, availableStock);
            throw new InsufficientStockException(item.getItemId(), quantityChange, availableStock);
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        log.info("Updated cartItem quantity and saved for itemId={} in cart id={}", item.getItemId(), item.getCartId());

        product.setQuantity(availableStock - quantityChange);
        productRepository.save(product);
        log.debug("Reduced stock for sku {} to {} after update", item.getItemId(), product.getQuantity());
    }

    public void clearCart(String username) {
        log.info("Clearing cart for user={}", username);
        cartRepository.findByUserId(username).ifPresentOrElse(
                cart -> {
                    log.info("Found cart id={} for user={}, clearing items", cart.getId(), username);
                    cart.getItems().forEach(cartItem -> {
                        productRepository.findBySku(cartItem.getItemId()).ifPresent(product -> {
                            product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                            productRepository.save(product);
                            log.debug("Restocked sku {} by {} units", cartItem.getItemId(), cartItem.getQuantity());
                        });
                    });
                    cartRepository.deleteByUserId(username);
                    log.info("Cleared cart id={} for user={}", cart.getId(), username);
                },
                () -> {
                    log.warn("No cart found for user={} when clearing", username);
                    throw CartNotFoundException.forUser(username);
                }
        );
    }

    public Cart<? extends CartItem> getCart(String username) {
        log.info("Fetching cart for user={}", username);
        Cart<? extends CartItem> result = cartRepository.findByUserId(username)
                .map(this::mapToCart)
                .orElseThrow(() -> {
                    log.warn("Cart not found for user={}", username);
                    return CartNotFoundException.forUser(username);
                });
        log.info("Cart fetching completed for user={}, returned {} items", username, result.getItems().size());
        return result;
    }

    public void removeItemFromCart(String username, String itemId) {
        log.info("Removing item from cart for user={}, itemId={}", username, itemId);
        cartRepository.findByUserId(username)
                .ifPresentOrElse(
                        cart -> {
                            Optional<CartItemEntity> cartItemOptional = cartItemRepository.findById(new CartItemPK(itemId, cart.getId()));
                            if (cartItemOptional.isPresent()) {
                                CartItemEntity cartItem = cartItemOptional.get();
                                cartItemRepository.deleteById(new CartItemPK(itemId, cart.getId()));
                                log.info("Removed cartItem itemId={} from cart id={}", itemId, cart.getId());

                                productRepository.findBySku(itemId).ifPresent(product -> {
                                    product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                                    productRepository.save(product);
                                    log.debug("Restocked sku {} by {} units after removal", itemId, cartItem.getQuantity());
                                });
                            }
                            else {
                                log.warn("CartItem itemId={} not found in cart id={}", itemId, cart.getId());
                                throw ItemNotFoundException.forItemId(itemId);
                            }
                        },
                        () -> {
                            log.warn("Cart not found for user={} when removing item", username);
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
