package com.celfocus.hiring.kickstarter.db.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CartItemPK implements Serializable {
    private Long cartId;
    private String itemId;

    public CartItemPK() {
    }

    public CartItemPK(String itemId, Long cartId) {
        this.itemId = itemId;
        this.cartId = cartId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
