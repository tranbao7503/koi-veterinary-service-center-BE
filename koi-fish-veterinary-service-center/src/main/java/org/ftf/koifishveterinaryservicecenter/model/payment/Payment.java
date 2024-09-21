package org.ftf.koifishveterinaryservicecenter.model.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Integer paymentId;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    @Column(name = "transaction_id", length = 100, nullable = true)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 6, scale = 2)
    private BigDecimal amount;

    @Lob
    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "status", nullable = true)
    @ColumnDefault("'NOT_PAID'")
    private String status;


}
