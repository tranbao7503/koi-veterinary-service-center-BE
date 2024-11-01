package org.ftf.koifishveterinaryservicecenter.service.voucherservice;

import org.ftf.koifishveterinaryservicecenter.entity.User;
import org.ftf.koifishveterinaryservicecenter.entity.Voucher;
import org.ftf.koifishveterinaryservicecenter.entity.user_voucher.UserVoucher;
import org.ftf.koifishveterinaryservicecenter.entity.user_voucher.UserVoucherId;
import org.ftf.koifishveterinaryservicecenter.exception.VoucherNotFoundException;
import org.ftf.koifishveterinaryservicecenter.exception.VoucherQuantityExceededException;
import org.ftf.koifishveterinaryservicecenter.repository.UserVoucherRepository;
import org.ftf.koifishveterinaryservicecenter.repository.VoucherRepository;
import org.ftf.koifishveterinaryservicecenter.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    private VoucherRepository voucherRepository;
    private UserVoucherRepository userVoucherRepository;
    private UserService userService;

    @Autowired
    public VoucherServiceImpl(
            VoucherRepository voucherRepository
            , UserVoucherRepository userVoucherRepository
            , UserService userService) {
        this.voucherRepository = voucherRepository;
        this.userVoucherRepository = userVoucherRepository;
        this.userService = userService;
    }


    @Override
    public Voucher findVoucherById(Integer id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new VoucherNotFoundException("Voucher with id " + id + " not found"));
        return voucher;
    }

    @Override
    public List<Voucher> getAvailableVouchersByCustomerId(Integer customerId) {
        List<Voucher> vouchers = voucherRepository.getVoucherByUserrId(customerId);
        if (vouchers == null) {
            throw new VoucherNotFoundException("There is no available voucher for customer with id " + customerId);
        }
        return vouchers;
    }

    @Override
    public BigDecimal getVoucherAmountByVoucherId(Integer voucherId) throws VoucherNotFoundException {
        Voucher voucher = this.findVoucherById(voucherId);
        return voucher.getDiscountAmount();
    }

    @Override
    public Integer addVoucherForCustomer(Integer customerId, Integer voucherId) throws VoucherNotFoundException {
        Voucher voucher = this.findVoucherById(voucherId);
        User customer = userService.getCustomerById(customerId);
                UserVoucherId userVoucherId = new UserVoucherId();
        userVoucherId.setUserId(customerId);
        userVoucherId.setVoucherId(voucherId);
        Optional<UserVoucher> optionalUserVoucher = userVoucherRepository.findById(userVoucherId);

        UserVoucher userVoucher;

        if (optionalUserVoucher.isPresent()) { // Customer already have the voucher
            userVoucher = optionalUserVoucher.get();
            userVoucher.setQuantity(userVoucher.getQuantity() + 1);
        } else {
            userVoucher = new UserVoucher();
            userVoucher.setUserVoucherId(userVoucherId);
            userVoucher.setQuantity(1);
            userVoucher.setVoucher(voucher);
            userVoucher.setUser(customer);
        }

        System.out.println(userVoucher.getQuantity());

        userVoucherRepository.save(userVoucher);

        return voucherId;
    }

    @Override
    public Integer subtractAVoucherOfCustomer(Integer customerId, Integer voucherId) throws VoucherNotFoundException {
        this.findVoucherById(voucherId);
        UserVoucherId userVoucherId = new UserVoucherId();
        userVoucherId.setUserId(customerId);
        userVoucherId.setVoucherId(voucherId);
        Optional<UserVoucher> optionalUserVoucher = userVoucherRepository.findById(userVoucherId);

        if (optionalUserVoucher.isPresent()) { // Customer already have the voucher
            UserVoucher userVoucher = optionalUserVoucher.get();
            if (userVoucher.getQuantity() == 0) { // Voucher not available
                throw new VoucherQuantityExceededException("Customer has no vouchers left to subtract");
            }
            userVoucher.setQuantity(userVoucher.getQuantity() - 1);
            userVoucherRepository.save(userVoucher);
        } else {
            throw new VoucherNotFoundException("There is no available voucher with id " + voucherId);
        }

        return voucherId;
    }
}
