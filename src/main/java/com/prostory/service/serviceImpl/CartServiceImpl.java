package com.prostory.service.serviceImpl;

import com.prostory.dto.request.CartItemRequest;
import com.prostory.dto.response.CartItemResponse;
import com.prostory.dto.response.CartResponse;
import com.prostory.entity.Cart;
import com.prostory.entity.CartItem;
import com.prostory.entity.Product;
import com.prostory.entity.User;
import com.prostory.exception.exceptions.NotFoundException;
import com.prostory.repository.CartRepository;
import com.prostory.repository.ProductRepository;
import com.prostory.repository.UserRepository;
import com.prostory.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getCart(String email) {
        Cart cart = getOrCreateCart(email);
        return mapToResponse(cart);
    }

    @Override
    public CartResponse addToCart(String email, CartItemRequest request) {
        Cart cart = getOrCreateCart(email);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.productId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + request.quantity()),
                        () -> cart.getItems().add(CartItem.builder()
                                .cart(cart)
                                .product(product)
                                .quantity(request.quantity())
                                .build())
                );

        return mapToResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateQuantity(String email, Long cartItemId, int quantity) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"))
                .setQuantity(quantity);

        return mapToResponse(cartRepository.save(cart));
    }

    @Override
    public void removeFromCart(String email, Long cartItemId) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImage(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, total);
    }
}
