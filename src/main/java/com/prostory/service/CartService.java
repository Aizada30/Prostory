package com.prostory.service;

import com.prostory.dto.request.CartItemRequest;
import com.prostory.dto.response.CartResponse;

public interface CartService {
    CartResponse getCart(String email);
    CartResponse addToCart(String email, CartItemRequest request);
    CartResponse updateQuantity(String email, Long cartItemId, int quantity);
    void removeFromCart(String email, Long cartItemId);
    void clearCart(String email);
}
