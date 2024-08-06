package com.rental.movie.service;

import com.rental.movie.config.VNPayConfig;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.util.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

@Service
public class VNPayService {
    @Autowired
    private VNPayConfig vnPayConfig;
//
    @Autowired
    private HttpServletRequest request;

    private VNPayUtils vnPayUtils;
//
//    public String createPaymentRequest(Invoice invoice) {
//
//        String bankCode = request.getParameter("bankCode");
//
//        Map<String, String> vnpParams = new TreeMap<>();
//        vnpParams.put("vnp_Version", "2.1.0");
//        vnpParams.put("vnp_Command", "pay");
//        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());
//        vnpParams.put("vnp_Amount", String.format("%.0f", invoice.getTotalPrice() * 100)); // VNPay yêu cầu số tiền tính bằng đồng
//        vnpParams.put("vnp_CurrCode", "VND");
//
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnpParams.put("vnp_BankCode", bankCode);
//        }
//
//        vnpParams.put("vnp_TxnRef", invoice.getId());
//        vnpParams.put("vnp_OrderInfo", "Payment for invoice " + invoice.getId());
//        vnpParams.put("vnp_OrderType", "other");
//        vnpParams.put("vnp_Locale", "vn");
//        vnpParams.put("vnp_ReturnUrl", "https://hoctuancustomservice.io.vn/payment/callback");
//
//        // Thêm địa chỉ IP
//        String ipAddress = getIpAddress(request);
//        vnpParams.put("vnp_IpAddr", ipAddress);
//
//        vnpParams.put("vnp_CreateDate", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
//
//        // Thêm thời gian hết hạn
//        ZonedDateTime expireDate = ZonedDateTime.now().plusHours(24);//24 tiếng từ khi tạo hóa đơn
//        vnpParams.put("vnp_ExpireDate", expireDate.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
//
////        // Tạo chữ ký (signature)
////        String vnpSecureHash = VNPayUtils.generateSignature(vnpParams, vnPayConfig.getHashSecret());
////        vnpParams.put("vnp_SecureHash", vnpSecureHash);
////
////        // Chuyển đổi Map thành MultiValueMap
////        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
////        vnpParams.forEach((key, value) -> multiValueMap.add(key, value));
////
////        // Tạo URL thanh toán
////        String url = UriComponentsBuilder.fromHttpUrl(vnPayConfig.getVnpUrl())
////                .queryParams(multiValueMap)
////                .build()
////                .toUriString();
//
//        List fieldNames = new ArrayList(vnpParams.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = (String) itr.next();
//            String fieldValue = (String) vnpParams.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = vnPayConfig.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = vnPayConfig.getVnpUrl() + "?" + queryUrl;
//
//        return paymentUrl;
//    }
//
    public boolean validatePayment(Map<String, String> params) {
        String vnpSecureHash = params.remove("vnp_SecureHash");
        String generatedSignature = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(),params.toString());

        return vnpSecureHash.equalsIgnoreCase(generatedSignature);
    }
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public String createPaymentRequest(Invoice invoice){
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = request.getParameter("bankCode");

        String vnp_TmnCode = vnPayConfig.getTmnCode();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.format("%.0f", invoice.getTotalPrice() * 100)); // VNPay yêu cầu số tiền tính bằng đồng
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", invoice.getId());
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + invoice.getId());
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());


        vnp_Params.put("vnp_IpAddr", getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());

        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnPayConfig.getVnpUrl()+ "?" + queryUrl;
        return paymentUrl;
    }

}
