package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.model.entity.Comment;
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
    private FilmRepository filmRepository;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO) {
        Comment comment = commentMapper.convertToEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.convertToDTO(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            throw new CustomException("Không có bình luận nào", HttpStatus.NOT_FOUND.value());
        }
        return comments.stream().map(commentMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDTO getCommentById(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bình luận", HttpStatus.NOT_FOUND.value()));
        return commentMapper.convertToDTO(comment);
    }

    @Override
    @Transactional
    public CommentResponseDTO updateComment(String id, CommentRequestDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy bình luận", HttpStatus.NOT_FOUND.value()));
        commentMapper.convertToEntity(commentDTO, existingComment);
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
    public List<CommentResponseDTO> getCommentsByFilmId(String filmId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        return film.getComments()
                .stream()
                .map(commentMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
