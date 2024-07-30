package com.rental.movie.util.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public CommentDTO convertToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    public Comment convertToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    public Comment convertToEntity(CommentDTO dto, Comment comment) {
        modelMapper.map(dto, comment);
        return comment;
    }
}
