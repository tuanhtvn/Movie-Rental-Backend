package com.rental.movie.controller;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class CategoryController {
        @Autowired
        private CategoryService categoryService;

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy danh sách các danh mục có phân trang", description = "Lấy ra tất cả danh mục không bao gồm danh mục đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các danh mục.")
        @GetMapping("category/getAll")
        public ResponseEntity<BaseResponse> getAllCategoriesPage(
                        @ParameterObject Pageable pageable,
                        @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                                "Tìm thấy danh sách các danh mục",
                                HttpStatus.OK.value(),
                                categoryService.getAll(pageable, search)));
        }

        @Operation(summary = "Lấy danh sách các danh mục không phân trang", description = "Lấy ra tất cả danh mục đang được active và không bao gồm danh mục đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các danh mục.")
        @GetMapping("auth/category/getAllActive")
        public ResponseEntity<BaseResponse> getAllActiveCategoriesPage(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách các danh mục",
                        HttpStatus.OK.value(),
                        categoryService.getAllActiveCategories(pageable, search)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy danh sách các danh mục không phân trang", description = "Lấy ra tất cả danh mục đang không active và không bao gồm danh mục đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách các danh mục.")
        @GetMapping("category/getAllInActive")
        public ResponseEntity<BaseResponse> getAllInActiveCategoriesPage(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách các danh mục",
                        HttpStatus.OK.value(),
                        categoryService.getAllInactiveCategories(pageable, search)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Lấy danh mục đã xoá mềm", description = "Lấy danh sách các danh mục đã xoá mềm")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh sách danh mục.")
        @GetMapping("category/getAllSoftDelete")
        public ResponseEntity<BaseResponse> getAllSoftDeletedCategories(
                @ParameterObject Pageable pageable,
                @RequestParam(defaultValue = "") String search
        ) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        "Tìm thấy danh sách danh mục",
                        HttpStatus.OK.value(),
                        categoryService.getAllSoftDeletedCategories(pageable, search)));
        }

        @Operation(summary = "Lấy danh mục theo Id", description = "Lấy danh mục theo Id danh mục")
        @ApiResponse(responseCode = "200", description = "Tìm thấy danh mục.")
        @GetMapping("auth/category/getOne/{categoryId}")
        public ResponseEntity<BaseResponse> getCategoryById(@PathVariable String categoryId) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                                "Tìm thấy danh mục",
                                HttpStatus.OK.value(),
                                categoryService.getCategoryById(categoryId)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Tạo mới danh mục", description = "Tạo mới một danh mục")
        @ApiResponse(responseCode = "201", description = "Tạo danh mục mới thành công.")
        @PostMapping("category/create")
        public ResponseEntity<BaseResponse> createCategory(@Valid @RequestBody CategoryRequestDTO categoryDTO) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponse(
                                "Tạo danh mục mới thành công.",
                                HttpStatus.CREATED.value(),
                                categoryService.createCategory(categoryDTO)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Cập nhật danh mục", description = "Cập nhật danh mục theo Id danh mục")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
                        @ApiResponse(responseCode = "200", description = "Cập nhật danh mục thành công.")
        })
        @PutMapping("category/update/{categoryId}")
        public ResponseEntity<BaseResponse> updateCategory(
                        @PathVariable String categoryId,
                        @Valid @RequestBody CategoryRequestDTO categoryDTO) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                                "Cập nhật danh mục thành công.",
                                HttpStatus.OK.value(),
                                categoryService.updateCategory(categoryId, categoryDTO)));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Xoá mềm danh mục", description = "Xoá mềm danh mục theo Id danh mục")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
                        @ApiResponse(responseCode = "200", description = "Xoá mềm danh mục thành công.")
        })
        @PatchMapping("category/softDelete/{categoryId}")
        public ResponseEntity<BaseResponse> softDeleteById(@PathVariable String categoryId) {
                categoryService.softDeleteCategory(categoryId);
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                                "Xoá mềm danh mục thành công",
                                HttpStatus.OK.value(),
                                null));
        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Khôi phục danh mục", description = "Khôi phục danh mục đã xoá mềm")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
                        @ApiResponse(responseCode = "200", description = "Khôi phục danh mục thành công.")
        })
        @PatchMapping("category/restore/{categoryId}")
        public ResponseEntity<BaseResponse> restoreById(@PathVariable String categoryId) {
                categoryService.restoreCategory(categoryId);
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                                "Khôi phục danh mục thành công",
                                HttpStatus.OK.value(),
                                null));
        }

//        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//        @Operation(summary = "Activate danh mục", description = "Activate danh mục theo Id danh mục")
//        @ApiResponses(value = {
//                        @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
//                        @ApiResponse(responseCode = "200", description = "Activate danh mục thành công.")
//        })
//        @PatchMapping("category/activate/{categoryId}")
//        public ResponseEntity<BaseResponse> activateById(@PathVariable String categoryId) {
//                categoryService.activateCategory(categoryId);
//                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                                "Activate danh mục thành công",
//                                HttpStatus.OK.value(),
//                                null));
//        }
//
//        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
//        @Operation(summary = "Deactivate danh mục", description = "Deactivate danh mục theo Id danh mục")
//        @ApiResponses(value = {
//                        @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
//                        @ApiResponse(responseCode = "200", description = "Deactivate danh mục thành công.")
//        })
//        @PatchMapping("category/deactivate/{categoryId}")
//        public ResponseEntity<BaseResponse> deactivateById(@PathVariable String categoryId) {
//                categoryService.deactivateCategory(categoryId);
//                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
//                                "Khôi phục danh mục thành công",
//                                HttpStatus.OK.value(),
//                                null));
//        }

        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
        @Operation(summary = "Thay đổi trạng thái active/deactive danh mục", description = "Active/Deactivate danh mục theo Id danh mục")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "404", description = "Không tìm thấy danh mục!"),
                @ApiResponse(responseCode = "200", description = "Active/Deactivate danh mục thành công.")
        })
        @PatchMapping("category/toggle-active-status/{categoryId}")
        public ResponseEntity<BaseResponse> deactivateById(@PathVariable String categoryId) {
                boolean result = categoryService.toggleActiveStatus(categoryId);
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(
                        result ? "Active danh mục thành công" : "Deactive danh mục thành công",
                        HttpStatus.OK.value(),
                        null));
        }
}
