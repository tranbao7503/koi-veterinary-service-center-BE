package org.ftf.koifishveterinaryservicecenter.repository;

import org.ftf.koifishveterinaryservicecenter.entity.user_voucher.UserVoucher;
import org.ftf.koifishveterinaryservicecenter.entity.user_voucher.UserVoucherId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, UserVoucherId> {
}
