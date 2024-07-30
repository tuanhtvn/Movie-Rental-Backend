package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.rental.movie.model.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;
import com.rental.movie.common.BaseResponse;
import com.rental.movie.service.CommentService;
import com.rental.movie.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Thêm bình luận mới", description = "Thêm bình luận mới cho phim")
    @ApiResponse(responseCode = "200", description = "Thêm bình luận thành công")
    @PostMapping
    public ResponseEntity<BaseResponse> createComment(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.createComment(commentDTO);
            BaseResponse response = new BaseResponse("Thêm bình luận thành công", HttpStatus.OK.value(), createdComment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách tất cả bình luận", description = "Lấy danh sách tất cả bình luận")
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllComments() {
        try {
            List<CommentDTO> comments = commentService.getAllComments();
            BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(), comments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy bình luận theo ID", description = "Lấy bình luận theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy bình luận thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bình luận")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getCommentById(@PathVariable String id) {
        try {
            CommentDTO comment = commentService.getCommentById(id);
            BaseResponse response = new BaseResponse("Lấy bình luận thành công", HttpStatus.OK.value(), comment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Cập nhật bình luận theo ID", description = "Cập nhật bình luận theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật bình luận thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bình luận")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable String id, @RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
            BaseResponse response = new BaseResponse("Cập nhật bình luận thành công", HttpStatus.OK.value(), updatedComment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Xóa bình luận theo ID", description = "Xóa bình luận theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa bình luận thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bình luận")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable String id) {
        try {
            commentService.deleteComment(id);
            BaseResponse response = new BaseResponse("Xóa bình luận thành công", HttpStatus.OK.value(), null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @Operation(summary = "Lấy danh sách bình luận theo ID phim", description = "Lấy danh sách bình luận theo ID phim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bình luận")
    })
    @GetMapping("/film/{filmId}")
    public ResponseEntity<BaseResponse> getCommentsByFilmId(@PathVariable String filmId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByFilmId(filmId);
            BaseResponse response = new BaseResponse("Lấy danh sách thành công", HttpStatus.OK.value(), comments);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            BaseResponse response = new BaseResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
