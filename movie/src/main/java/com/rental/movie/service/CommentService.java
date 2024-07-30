package com.rental.movie.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rental.movie.model.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;

public interface CommentService {
    public CommentDTO createComment(CommentDTO commentDTO);
    public List<CommentDTO> getAllComments();
    public CommentDTO getCommentById(String id);
    public CommentDTO updateComment(String id, CommentDTO commentDTO);
    public void deleteComment(String id);
    public List<CommentDTO> getCommentsByFilmId(String filmId);
}
