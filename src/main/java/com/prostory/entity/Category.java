package com.prostory.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @SequenceGenerator(name = "category_gen", sequenceName = "category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_gen")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
}
