package com.celfocus.hiring.kickstarter.api.dto;

import java.math.BigDecimal;

public record CartItemResponse(String itemId, int quantity, BigDecimal price, String name) {
}
