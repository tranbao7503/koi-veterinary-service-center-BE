package org.ftf.koifishveterinaryservicecenter.service.paymentservice;

import org.ftf.koifishveterinaryservicecenter.configuration.PaymentConfig;
import org.ftf.koifishveterinaryservicecenter.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    private final PaymentConfig paymentConfig;

    @Autowired
    public VnPayService(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    /*
     * Create URL for VNPay payment request
     * */
    public String createPaymentUrl(Appointment appointment) throws Exception {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        // Create random Payment ID
        String vnp_TxnRef = appointment.getAppointmentId() + "-" + UUID.randomUUID().toString();
        String vnp_IpAddr = "127.0.0.1";

        String vnp_OrderInfo = generateOrderInfo(appointment); // Create Order Information

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", paymentConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(appointment.getPayment().getAmount().multiply(BigDecimal.valueOf(100)).longValue()));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", paymentConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        String hashData = buildHashData(vnp_Params); // Build hash data

        String vnp_SecureHash = hmacSHA512(paymentConfig.getHashSecret(), hashData); // Create vnp_SecureHash

        StringBuilder query = new StringBuilder(hashData); // Create last URL

        query.append("&vnp_SecureHash=").append(vnp_SecureHash); // Insert vnp_SecureHash into URL

        String paymentUrl = paymentConfig.getApiUrl() + "?" + query.toString(); // Finish URL

        return paymentUrl;
    }

    /*
     * Verify HASH after finish payment via VNPay
     * */
    public boolean verifySignature(Map<String, String> vnpParams) throws UnsupportedEncodingException, Exception {
        String vnp_SecureHash = vnpParams.get("vnp_SecureHash");
        Map<String, String> filteredParams = new HashMap<>(vnpParams);
        filteredParams.remove("vnp_SecureHash");
        filteredParams.remove("vnp_SecureHashType");

        // Build hash data from filtered params
        String hashData = buildHashData(filteredParams);

        // Create hash from secret key
        String computedHash = hmacSHA512(paymentConfig.getHashSecret(), hashData);

        // Compare VNPay hash with computed hash
        return vnp_SecureHash.equals(computedHash);
    }

    private static String hmacSHA512(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return toHexString(hash);
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /*
     * Create Hash Data
     * */
    private String buildHashData(Map<String, String> params) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                hashData.append('&');
            }
        }

        if (hashData.length() > 0) {  // Remove last '&'
            hashData.deleteCharAt(hashData.length() - 1);
        }

        return hashData.toString();
    }

    private String generateOrderInfo(Appointment appointment) throws Exception {
        String description = String.format("Thanh toán dịch vụ %s mã đơn %d", appointment.getService().getServiceName(), appointment.getAppointmentId());
        return URLEncoder.encode(description, "UTF-8");
    }

    public Integer getAppointmentIdFromTxnRef(String txnRef) {
        String[] parts = txnRef.split("-");
        if (parts.length > 0) {
            return Integer.valueOf(parts[0]);
        }
        return null;
    }
}
