package com.prostory.entity;

import com.prostory.enums.DeliveryType;
import com.prostory.enums.OrderStatus;
import com.prostory.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
    @Table(name = "orders")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
        private List<OrderItem> items;

        private String fullName;
        private String phone;
        private String email;
        private String address;

        @Enumerated(EnumType.STRING)
        private DeliveryType deliveryType;

        @Enumerated(EnumType.STRING)
        private PaymentType paymentType;

        @Enumerated(EnumType.STRING)
        private OrderStatus status;

        private BigDecimal totalPrice;
        private LocalDate createdAt;
}
