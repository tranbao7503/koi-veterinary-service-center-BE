package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    @Query("SELECT v FROM Voucher v JOIN UserVoucher uv ON v.id = uv.userVoucherId.voucherId " +
            "WHERE uv.userVoucherId.userId = :userId AND uv.quantity > 0")
    List<Voucher> getVoucherByUserrId(Integer userId);
}
