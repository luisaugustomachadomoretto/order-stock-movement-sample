package com.sibis.order.OrderService.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "TB_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    @Column(nullable = false,name = "name")
    private String name;

    @NonNull
    @Column(nullable = false,name = "email", unique = true)
    private String email;
}
