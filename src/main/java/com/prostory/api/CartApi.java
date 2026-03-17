package com.prostory.api;

import com.prostory.dto.request.CartItemRequest;
import com.prostory.dto.response.CartResponse;
import com.prostory.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApi{
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getCart(principal.getName()));
    }

    @PostMapping("/addToCart")
    public ResponseEntity<CartResponse> addToCart(Principal principal,
                                                  @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(principal.getName(), request));
    }

    @PutMapping("/updateQuantity/{cartItemId}")
    public ResponseEntity<CartResponse> updateQuantity(Principal principal,
                                                       @PathVariable Long cartItemId,
                                                       @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(principal.getName(), cartItemId, quantity));
    }

    @DeleteMapping("/removeFromCart/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(Principal principal,
                                               @PathVariable Long cartItemId) {
        cartService.removeFromCart(principal.getName(), cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return ResponseEntity.noContent().build();
    }
}
