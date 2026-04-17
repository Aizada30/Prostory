package com.prostory.dto.response;

import com.prostory.enums.DeliveryType;
import com.prostory.enums.OrderStatus;
import com.prostory.enums.PaymentType;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long orderId,
        List<CartItemResponse> items,  // уже есть у тебя!
        BigDecimal totalPrice,
        int totalQuantity,
        DeliveryType deliveryType,
        PaymentType paymentType,
        OrderStatus status
) {
}
