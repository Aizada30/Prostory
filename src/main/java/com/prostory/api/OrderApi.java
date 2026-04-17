package com.prostory.api;

import com.prostory.dto.request.OrderRequest;
import com.prostory.dto.response.OrderResponse;
import com.prostory.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApi {
    private final OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<OrderResponse> createOrder(Principal principal,
                                                     @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(principal.getName(), request));
    }
}