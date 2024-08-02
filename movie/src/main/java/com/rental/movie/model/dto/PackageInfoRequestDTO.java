package com.rental.movie.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PackageInfoRequestDTO extends BaseRequestDTO{
    @NotBlank(message = "Bạn chưa nhập tên cho gói thuê!")
    @Schema(description = "Tên gói thuê", example = "Gói Cao Cấp")
    private String packageName; // tên gói

    @NotBlank(message = "Bạn chưa nhập mô tả cho gói thuê!")
    @Schema(description = "Mô tả gói thuê", example = "Đây là gói cao cấp")
    private String description; // mô tả

    @NotNull(message = "Bạn chưa nhập giá cho gói thuê!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá gói thuê phải lớn hơn 0")
    @Schema(description = "Giá gói thuê", example = "199.99")
    private Double price; // giá

    @NotNull(message = "Bạn chưa nhập thời gian sử dụng cho gói thuê!")
    @Min(value = 1, message = "Thời gian sử dụng gói thuê phải lớn hơn 0")
    @Schema(description = "Thời gian sử dụng gói thuê", example = "24")
    private Integer timeDuration; // thời gian sử dụng (ngày)
}

