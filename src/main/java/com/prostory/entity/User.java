package com.prostory.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq",
            allocationSize = 1, initialValue = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    private Long id;
    private String userName;
    private String phoneNumber;
    private String address;

    @OneToOne(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;
    @OneToOne(cascade = ALL, mappedBy = "user", orphanRemoval = true)
    private Favorite favorite;
}
