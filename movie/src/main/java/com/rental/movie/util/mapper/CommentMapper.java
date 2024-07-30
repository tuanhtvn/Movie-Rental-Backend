package com.rental.movie.util.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.rental.movie.model.dto.CommentRequestDTO;
import com.rental.movie.model.dto.CommentResponseDTO;
import com.rental.movie.repository.CommentRepository;
import com.rental.movie.model.entity.Comment;

@Component
public class CommentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CommentResponseDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentResponseDTO.class);
    }

    public Comment convertToEntity(CommentRequestDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    public Comment convertToEntity(CommentRequestDTO dto, Comment comment) {
        modelMapper.map(dto, comment);
        return comment;
    }
}
