package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rental.movie.model.entity.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;

public interface CommentService {
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO);
    public List<CommentResponseDTO> getAllComments();
    public CommentResponseDTO getCommentById(String id);
    public CommentResponseDTO updateComment(String id, CommentRequestDTO commentDTO);
    public void deleteComment(String id);
    //public List<CommentResponseDTO> getCommentsByFilmId(String filmId);
}
