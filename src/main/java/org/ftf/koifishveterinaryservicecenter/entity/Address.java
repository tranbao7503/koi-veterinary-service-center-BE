package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Integer addressId;

    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @Column(name = "district", length = 50, nullable = false)
    private String district;

    @Column(name = "ward", length = 50, nullable = false)
    private String ward;

    @Column(name = "home_number", length = 50, nullable = false)
    private String homeNumber;

    @ManyToOne
    @JoinColumn(name = "user_id") // or whatever your foreign key column is
    private User customer;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

}
