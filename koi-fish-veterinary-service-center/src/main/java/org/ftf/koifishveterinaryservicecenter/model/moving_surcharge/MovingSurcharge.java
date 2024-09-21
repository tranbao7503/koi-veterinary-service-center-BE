package org.ftf.koifishveterinaryservicecenter.model.moving_surcharge;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "moving_surcharge")
public class MovingSurcharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer movingSurchargeId;

    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @Column(name = "price", nullable = false, precision = 6, scale = 2)
    private BigDecimal price;



}
