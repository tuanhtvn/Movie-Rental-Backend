package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rental.movie.model.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;
import com.rental.movie.exception.CustomException;
import com.rental.movie.repository.CommentRepository;
import com.rental.movie.util.mapper.CommentMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.convertToDTO(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            throw new CustomException("Không có bình luận nào", HttpStatus.NOT_FOUND.value());
        }
        return comments.stream().map(commentMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDTO getCommentById(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bình luận", HttpStatus.NOT_FOUND.value()));
        return commentMapper.convertToDTO(comment);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(String id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bình luận", HttpStatus.NOT_FOUND.value()));
        commentMapper.updateEntity(commentDTO, existingComment);
        existingComment = commentRepository.save(existingComment);
        return commentMapper.convertToDTO(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bình luận", HttpStatus.NOT_FOUND.value()));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByFilmId(String filmId) {
        List<Comment> comments = commentRepository.findByFilmId(filmId);
        if (comments.isEmpty()) {
            throw new CustomException("Không có bình luận nào cho phim này", HttpStatus.NOT_FOUND.value());
        }
        return comments.stream().map(commentMapper::convertToDTO).collect(Collectors.toList());
    }
}
