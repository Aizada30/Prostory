package com.prostory.service;

import com.prostory.dto.request.OrderRequest;
import com.prostory.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(String email, OrderRequest request);
}
