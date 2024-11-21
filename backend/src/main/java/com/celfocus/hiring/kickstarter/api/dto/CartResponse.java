package com.celfocus.hiring.kickstarter.api.dto;

import java.util.List;

public record CartResponse(List<CartItemResponse> items) {
}
