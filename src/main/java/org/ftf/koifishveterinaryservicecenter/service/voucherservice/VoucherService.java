package org.ftf.koifishveterinaryservicecenter.service.voucherservice;

import org.ftf.koifishveterinaryservicecenter.entity.Voucher;
import org.ftf.koifishveterinaryservicecenter.exception.VoucherNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

    Voucher findVoucherById(Integer id);

    List<Voucher> getAvailableVouchersByCustomerId(Integer customerId);

    BigDecimal getVoucherAmountByVoucherId(Integer voucherId) throws VoucherNotFoundException;

    Integer addVoucherForCustomer(Integer customerId, Integer voucherId) throws VoucherNotFoundException;

    Integer subtractAVoucherOfCustomer(Integer customerId, Integer voucherId) throws VoucherNotFoundException;

}
