package com.prostory.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @SequenceGenerator(name = "product_gen", sequenceName = "product_seq",
            allocationSize = 1, initialValue = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    private Long id;

    private String article;
    private String name;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;
    private int discountPercent;
    private int quantity;
    private double rating;
    private LocalDate createdAt;

    @Column(length = 1000000)
    private String image;

    private boolean inStock;

    @OneToMany(cascade = ALL, mappedBy = "product", orphanRemoval = true)
    private List<Review> reviews;

    @ManyToOne(cascade = {DETACH, MERGE, REFRESH, PERSIST})
    @JoinColumn(name = "category_id")
    private Category category;
}
