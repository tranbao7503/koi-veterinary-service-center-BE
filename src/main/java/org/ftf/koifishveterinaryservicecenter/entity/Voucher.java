package org.ftf.koifishveterinaryservicecenter.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.entity.user_voucher.UserVoucher;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Integer id;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "voucher")
    private Set<UserVoucher> users = new LinkedHashSet<>();

}
