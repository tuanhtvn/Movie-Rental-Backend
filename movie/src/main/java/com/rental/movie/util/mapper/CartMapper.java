package com.rental.movie.util.mapper;

import com.rental.movie.exception.NotFoundException;
import com.rental.movie.model.dto.CartRequestDTO;
import com.rental.movie.model.dto.CartResponseDTO;
import com.rental.movie.model.entity.Film;
import com.rental.movie.model.entity.Item;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    @Autowired
    private FilmMapper filmMapper;

    public CartResponseDTO convertToDTO(User user) {
        CartResponseDTO cartDTO = new CartResponseDTO();
        cartDTO.setFilms(new ArrayList<>()); // Khởi tạo danh sách films

        for (Item item : user.getCart()) {
            if (item.getFilm().getIsDeleted())
                user.getCart().remove(item);
            else
                cartDTO.getFilms().add(filmMapper.convertToDTO(item.getFilm()));
        }
        return cartDTO;
    }

}
