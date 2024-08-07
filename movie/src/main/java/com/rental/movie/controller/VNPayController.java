package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.common.PaymentStatus;
import com.rental.movie.common.TransactionStatus;
import com.rental.movie.model.entity.Invoice;
import com.rental.movie.model.entity.TransactionHistory;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.InvoiceRepository;
import com.rental.movie.repository.PackageInfoRepository;
import com.rental.movie.repository.TransactionHistoryRepository;
import com.rental.movie.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.bson.types.ObjectId;
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

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

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
    public ResponseEntity<BaseResponse> handlePaymentCallback(@RequestParam Map<String, String> params) {
        boolean isValid = vnPayService.validatePayment(params);

        String invoiceId = params.get("vnp_TxnRef");
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("Invoice not found"));
        User user = invoice.getUser();
        BaseResponse baseResponse;
        if (params.get("vnp_ResponseCode").equals("00") && isValid){
            updateData(user,invoice,PaymentStatus.SUCCESS,TransactionStatus.SUCCESS);
            baseResponse = BaseResponse.builder()
                    .message("Thanh tooán thành công")
                    .status(HttpStatus.OK.value())
                    .data(params)
                    .build();
        }

        else{
            updateData(user,invoice,PaymentStatus.FAILED,TransactionStatus.FAILED);
            baseResponse = BaseResponse.builder()
                    .message("Thanh toán không thành công")
                    .status(HttpStatus.OK.value())
                    .data(params)
                    .build();
        }

        userService.save(user);
        invoiceRepository.save(invoice);

        return ResponseEntity.status(HttpStatus.OK).body(baseResponse);
    }

    private void updateData(User user, Invoice invoice, PaymentStatus paymentStatus, TransactionStatus transactionStatus){
        // Cập nhật trạng thái của hóa đơn
        invoice.setPaymentStatus(paymentStatus);
        // Thêm lịch sử giao dịch vào danh sách lịch sử giao dịch của user
        TransactionHistory transactionHistory = new TransactionHistory(new ObjectId().toString(),
                ZonedDateTime.now(),
                invoice.getTotalPrice(),
                transactionStatus);
        user.getTransactionHistories().add(transactionHistory);
        if(invoice.getPackageInfo() == null)
            rentalService.addRentedFilm(invoice.getFilms(),user);
        else
            rentalService.addRentalPackage(invoice.getPackageInfo(),user);
    }
}
