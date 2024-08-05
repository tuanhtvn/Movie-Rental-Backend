package com.rental.movie.service;

import com.rental.movie.config.VNPayConfig;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.util.VNPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class VNPayService {
    @Autowired
    private VNPayConfig vnPayConfig;

    public String createPaymentRequest(Invoice invoice) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnpParams.put("vnp_Amount", String.format("%.0f", invoice.getTotalPrice() * 100)); // VNPay yêu cầu số tiền tính bằng đồng
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", invoice.getId());
        vnpParams.put("vnp_OrderInfo", "Payment for invoice " + invoice.getId());
        vnpParams.put("vnp_OrderType", "billpayment");
        vnpParams.put("vnp_ReturnUrl", "http://yourdomain.com/payment/callback");
        vnpParams.put("vnp_Version", "2.0.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_CreateDate", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        // Tạo chữ ký (signature)
        String vnpSecureHash = VNPayUtils.generateSignature(vnpParams, vnPayConfig.getHashSecret());
        vnpParams.put("vnp_SecureHash", vnpSecureHash);

        // Chuyển đổi Map thành MultiValueMap
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        vnpParams.forEach((key, value) -> multiValueMap.add(key, value));

        // Tạo URL thanh toán
        String url = UriComponentsBuilder.fromHttpUrl(vnPayConfig.getVnpUrl())
                .queryParams(multiValueMap)
                .build()
                .toUriString();

        return url;
    }

    public boolean validatePayment(Map<String, String> params) {
        String vnpSecureHash = params.remove("vnp_SecureHash");
        String generatedSignature = VNPayUtils.generateSignature(params, vnPayConfig.getHashSecret());

        return vnpSecureHash.equalsIgnoreCase(generatedSignature);
    }
}
