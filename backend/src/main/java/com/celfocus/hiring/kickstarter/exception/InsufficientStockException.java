package com.celfocus.hiring.kickstarter.exception;

public class InsufficientStockException extends RuntimeException{

    private final String itemId;
    private final int requestedQuantity;
    private final int availableStock;

    public InsufficientStockException(String itemId, int requestedQuantity, int availableStock) {
        super(String.format("Insufficient stock for item %s. Requested: %d, Available: %d",
              itemId, requestedQuantity, availableStock));
        this.itemId = itemId;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public InsufficientStockException(String message) {
        super(message);
        this.itemId = null;
        this.requestedQuantity = 0;
        this.availableStock = 0;
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
        this.itemId = null;
        this.requestedQuantity = 0;
        this.availableStock = 0;
    }

    public String getItemId() {
        return itemId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
