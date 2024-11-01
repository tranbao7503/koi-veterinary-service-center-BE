package org.ftf.koifishveterinaryservicecenter.entity.user_voucher;

import jakarta.persistence.*;
import lombok.Data;
import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.entity.Voucher;

@Data
@Entity
@Table(name = "user_voucher")
public class UserVoucher {

    @EmbeddedId
    private UserVoucherId userVoucherId;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;

    @MapsId("voucherId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voucher_id", nullable = false, referencedColumnName = "voucher_id")
    private Voucher voucher;
}
