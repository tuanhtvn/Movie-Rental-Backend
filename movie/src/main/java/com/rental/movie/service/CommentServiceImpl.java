package com.rental.movie.service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.model.entity.Comment;
import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;
import com.rental.movie.model.dto.CommentDTO;
import com.rental.movie.exception.CustomException;
import com.rental.movie.repository.CommentRepository;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.UserRepository;
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
    private UserRepository userRepository;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDTO createComment(CommentRequestDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getIdUser())
                .orElseThrow(() -> new CustomException("Người dùng với ID " + commentDTO.getIdUser() + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Film film = filmRepository.findById(commentDTO.getFilmId())
                .orElseThrow(() -> new CustomException("Phim với ID " + commentDTO.getFilmId() + " không tồn tại", HttpStatus.NOT_FOUND.value()));

        Comment comment = commentMapper.convertToEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        film.getComments().add(savedComment);
        filmRepository.save(film);
        return commentMapper.convertToDTO(savedComment);
    }

    @Override
    public List<CommentResponseDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        if (comments.isEmpty()) {
            throw new CustomException("Không có bình luận nào", HttpStatus.NOT_FOUND.value());
        }
        return comments.stream().map(commentMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
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
    public Page<CommentResponseDTO> getCommentsByFilmId(String filmId, int page, int size) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByFilmId(filmId, pageable);
        return commentPage.map(commentMapper::convertToDTO);
    }

    @Override
    public Page<CommentDTO> getCommentFilm(String filmId, int page, int size, String currentUserId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new CustomException("Không tìm thấy phim", HttpStatus.NOT_FOUND.value()));

        Pageable pageable = PageRequest.of(page, size);

        Page<Comment> commentPage = commentRepository.findByFilmId(filmId, pageable);

        return commentPage.map(comment -> {
            User user = userRepository.findById(comment.getIdUser())
                    .orElseThrow(() -> new CustomException("Người dùng không tồn tại", HttpStatus.NOT_FOUND.value()));

            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setImgURL(user.getAvatar());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            commentDTO.setUserName(user.getFullName());
            commentDTO.setText(comment.getText());
            commentDTO.setIsMyComment(currentUserId.equals(comment.getIdUser()));

            return commentDTO;
        });
    }
}
