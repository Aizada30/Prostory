package com.prostory.dto.request;

import com.prostory.enums.DeliveryType;
import com.prostory.enums.PaymentType;

public record OrderRequest(
        String fullName,
        String phone,
        String email,
        String address,
        DeliveryType deliveryType,
        PaymentType paymentType) {
}
