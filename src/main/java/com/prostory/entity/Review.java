    package com.prostory.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;

    @Getter
    @Setter
    @Entity
    @Table(name = "reviews")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Review {
        @Id
        @SequenceGenerator(name = "review_gen", sequenceName = "review_seq",
                allocationSize = 1, initialValue = 5)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_gen")
        private Long id;

        private String text;
        private int rating;
        private LocalDate createdAt;

        @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                CascadeType.REFRESH, CascadeType.PERSIST})
        @JoinColumn(name = "product_id")
        private Product product;

        @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                CascadeType.REFRESH, CascadeType.PERSIST})
        @JoinColumn(name = "user_id")
        private User user;
    }
