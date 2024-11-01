package org.ftf.koifishveterinaryservicecenter.controller;

import org.ftf.koifishveterinaryservicecenter.dto.VoucherDto;
import org.ftf.koifishveterinaryservicecenter.entity.Voucher;
import org.ftf.koifishveterinaryservicecenter.exception.VoucherNotFoundException;
import org.ftf.koifishveterinaryservicecenter.mapper.VoucherMapper;
import org.ftf.koifishveterinaryservicecenter.service.userservice.AuthenticationService;
import org.ftf.koifishveterinaryservicecenter.service.voucherservice.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vouchers")
public class VoucherController {

    private final VoucherService voucherService;
    private final AuthenticationService authenticationService;

    @Autowired
    public VoucherController(
            VoucherService voucherService
            , AuthenticationService authenticationService) {
        this.voucherService = voucherService;
        this.authenticationService = authenticationService;
    }

    /*
     * Get list of available voucher for making appointment
     * Actors: Customer
     * */
    @GetMapping("/customer")
    public ResponseEntity<?> getAvailableVouchers() {
        try {
            Integer customerId = authenticationService.getAuthenticatedUserId();
            List<Voucher> vouchers = voucherService.getAvailableVouchersByCustomerId(customerId);
            List<VoucherDto> voucherDtos = vouchers.stream()
                    .map(VoucherMapper.INSTANCE::convertToVoucherDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(voucherDtos, HttpStatus.OK);
        } catch (VoucherNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Get amount of voucher
     * */
    @GetMapping("/{voucherId}/amount")
    public ResponseEntity<?> getVoucherAmount(
            @PathVariable("voucherId") Integer voucherId) {
        try {
            BigDecimal amount = voucherService.getVoucherAmountByVoucherId(voucherId);
            return new ResponseEntity<>(amount, HttpStatus.OK);
        } catch (VoucherNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    * For testing
    * Add voucher for customer
    * */
    @PostMapping("/{voucherId}/{customerId}")
    public ResponseEntity<?> addVoucher(
            @PathVariable("voucherId") Integer voucherId
    , @PathVariable("customerId") Integer customerId) {
        Integer updatedVoucherId = voucherService.addVoucherForCustomer(customerId, voucherId);
        return new ResponseEntity<>(updatedVoucherId, HttpStatus.OK);
    }

    /*
    * For testing
    * Subtract used voucher of customer
    * */
    @DeleteMapping("/{voucherId}")
    public ResponseEntity<?> deleteVoucher(
            @PathVariable("voucherId") Integer voucherId) {
        Integer customerId = authenticationService.getAuthenticatedUserId();
        Integer updatedVoucherId = voucherService.subtractAVoucherOfCustomer(customerId, voucherId);
        return new ResponseEntity<>(updatedVoucherId, HttpStatus.OK);
    }
}
