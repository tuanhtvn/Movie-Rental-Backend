package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.CartRequestDTO;
import com.rental.movie.model.dto.CartResponseDTO;
import com.rental.movie.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "Thêm phim vào giỏ hàng", description = "API thêm phim vào giỏ hàng")
    @ApiResponse(responseCode = "200", description = "Thêm phim vào giỏ hàng thành công")
    @PostMapping("/add/{filmId}")
    public ResponseEntity<BaseResponse> addToCart(@PathVariable("filmId") String filmId) {
        cartService.addToCart(filmId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Thêm phim vào giỏ hàng thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xem danh sách phim trong giỏ hàng", description = "API xem danh sách phim trong giỏ hàng")
    @ApiResponse(responseCode = "200", description = "Xem danh sách phim trong giỏ hàng thành công")
    @GetMapping("/view")
    public ResponseEntity<BaseResponse> viewCart() {
        CartResponseDTO cartResponseDTO = cartService.viewCart();
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xem danh sách phim trong giỏ hàng thành công")
                .data(cartResponseDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Xóa phim khỏi giỏ hàng", description = "API xóa phim khỏi giỏ hàng")
    @ApiResponse(responseCode = "200", description = "Xóa phim khỏi giỏ hàng thành công")
    @DeleteMapping("/remove/{filmId}")
    public ResponseEntity<BaseResponse> removeFromCart(@PathVariable("filmId") String filmId) {
        cartService.removeFromCart(filmId);
        BaseResponse response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa phim khỏi giỏ hàng thành công")
                .build();
        return ResponseEntity.ok(response);
    }
}
