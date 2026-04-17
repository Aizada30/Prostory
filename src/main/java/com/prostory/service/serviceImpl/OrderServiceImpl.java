package com.prostory.service.serviceImpl;

import com.prostory.dto.request.OrderRequest;
import com.prostory.dto.response.CartItemResponse;
import com.prostory.dto.response.OrderResponse;
import com.prostory.entity.Cart;
import com.prostory.entity.Order;
import com.prostory.entity.OrderItem;
import com.prostory.entity.User;
import com.prostory.enums.DeliveryType;
import com.prostory.enums.OrderStatus;
import com.prostory.exception.exceptions.NotFoundException;
import com.prostory.repository.CartRepository;
import com.prostory.repository.OrderRepository;
import com.prostory.repository.UserRepository;
import com.prostory.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(String email, OrderRequest request) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new NotFoundException("Cart is empty");
        }

        // берём товары из корзины и копируем в OrderItem
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalQuantity = orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();

        Order order = Order.builder()
                .user(user)
                .fullName(request.fullName())
                .phone(request.phone())
                .email(request.email())
                .address(request.deliveryType() == DeliveryType.PICKUP ? null : request.address())
                .deliveryType(request.deliveryType())
                .paymentType(request.paymentType())
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .createdAt(LocalDate.now())
                .build();

        // связываем OrderItem с Order
        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        orderRepository.save(order);

        // очищаем корзину после заказа
        cart.getItems().clear();
        cartRepository.save(cart);

        log.info("Order created successfully for user {}", email);

        return mapToResponse(order, totalQuantity);
    }

    private OrderResponse mapToResponse(Order order, int totalQuantity) {
        List<CartItemResponse> items = order.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImage(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                items,
                order.getTotalPrice(),
                totalQuantity,
                order.getDeliveryType(),
                order.getPaymentType(),
                order.getStatus()
        );
    }
}
