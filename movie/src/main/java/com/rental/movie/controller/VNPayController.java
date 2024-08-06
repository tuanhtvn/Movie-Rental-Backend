package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.PaymentStatus;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.model.entity.PackageInfo;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.repository.PackageInfoRepository;
import com.rental.movie.service.FilmService;
import com.rental.movie.service.InvoiceService;
import com.rental.movie.service.PackageInfoService;
import com.rental.movie.service.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Map;

@RestController
public class VNPayController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private PackageInfoRepository packageInfoRepository;

    @Operation(summary = "Tạo yêu cầu thanh toán VNPay danh sách các phim (Rentaltype = RENTAL)  trong giỏ hàng", description = "API tạo yêu cầu thanh toán VNPay danh sách các phim (Rentaltype = RENTAL) trong giỏ hàng")
    @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thanh toán thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/createCartPayment")
    public ResponseEntity<BaseResponse> createCartPayment() {
        Invoice invoice = invoiceService.createInvoice(null,null);
        // Tạo URL thanh toán VNPay
        String paymentUrl = vnPayService.createPaymentRequest(invoice);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tạo yêu cầu thanh toán thành công")
                .data(paymentUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tạo yêu cầu thanh toán VNPay của 1 phim lẻ (Rentaltype = RENTAL) ", description = "API tạo yêu cầu thanh toán VNPay của 1 phim lẻ (Rentaltype = RENTAL)")
    @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thanh toán thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/createFilmPayment/{filmId}")
    public ResponseEntity<BaseResponse> createFilmPayment(@PathVariable String filmId) {
        Invoice invoice = invoiceService.createInvoice(null,filmService.getById(filmId));
        // Tạo URL thanh toán VNPay
        String paymentUrl = vnPayService.createPaymentRequest(invoice);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tạo yêu cầu thanh toán thành công")
                .data(paymentUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tạo yêu cầu thanh toán VNPay của 1 gói thuê (Rentaltype = SUBSCRIPTION) ", description = "API tạo yêu cầu thanh toán VNPay của 1 gói thuê (Rentaltype = SUBSCRIPTION)")
    @ApiResponse(responseCode = "200", description = "Tạo yêu cầu thanh toán thành công")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/createPackageInfoPayment/{packageInfoId}")
    public ResponseEntity<BaseResponse> createPackageInfoPayment(@PathVariable String packageInfoId) {
        Invoice invoice = invoiceService.createInvoice(packageInfoRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(packageInfoId),null);
        // Tạo URL thanh toán VNPay
        String paymentUrl = vnPayService.createPaymentRequest(invoice);

        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Tạo yêu cầu thanh toán thành công")
                .data(paymentUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xử lý callback thanh toán VNPay", description = "API xử lý callback từ VNPay sau khi thanh toán")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xử lý callback thành công"),
            @ApiResponse(responseCode = "400", description = "Yêu cầu không hợp lệ")
    })
    @GetMapping("/api/auth/payment/callback")
    public String handlePaymentCallback(@RequestParam Map<String, String> params) {
        boolean isValid = vnPayService.validatePayment(params);

        String invoiceId = params.get("vnp_TxnRef");
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (isValid) {
            invoice.setPaymentStatus(PaymentStatus.SUCCESS);
            // Cập nhật trạng thái hóa đơn và thực hiện các hành động khác nếu cần
        } else {
            invoice.setPaymentStatus(PaymentStatus.FAILED);
        }

        invoiceRepository.save(invoice);
        return "Payment status updated";
    }
}
