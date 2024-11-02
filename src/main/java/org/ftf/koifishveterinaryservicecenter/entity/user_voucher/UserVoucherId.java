package org.ftf.koifishveterinaryservicecenter.entity.user_voucher;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Objects;

@Data
@Embeddable
public class UserVoucherId {

    @Column(name = "voucher_id", nullable = false)
    private Integer voucherId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVoucherId that = (UserVoucherId) o;
        return Objects.equals(voucherId, that.voucherId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voucherId, userId);
    }
}
