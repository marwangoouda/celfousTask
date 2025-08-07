package com.celfocus.hiring.kickstarter.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Cart<T extends CartItem> {
    private String userId;
    private List<T> items;
    private LocalDateTime lastModifiedDate;


    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
