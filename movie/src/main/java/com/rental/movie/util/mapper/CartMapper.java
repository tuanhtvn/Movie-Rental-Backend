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

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FilmRepository filmRepository;

    // Convert Item entity to CartResponseDTO.FilmDTO
    public CartResponseDTO.FilmDTO convertToDTO(Item item) {
        CartResponseDTO.FilmDTO filmDTO = new CartResponseDTO.FilmDTO();
        modelMapper.map(item.getFilm(), filmDTO);
        return filmDTO;
    }

    // Convert list of Item entities to CartResponseDTO
    public CartResponseDTO convertToDTO(User user) {
        List<CartResponseDTO.FilmDTO> filmDTOs = user.getCart().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setFilms(filmDTOs);
        return cartResponseDTO;
    }

    // Convert CartRequestDTO to Item entity
    public Item convertToEntity(CartRequestDTO dto) {
        Film film = filmRepository.findById(dto.getFilmId()).orElseThrow(
                () -> new NotFoundException("Không tìm thấy phim"));
        Item item = new Item();
        item.setFilm(film);
        return item;
    }

    // Convert list of CartRequestDTO to list of Item entities
    public List<Item> convertToEntity(List<CartRequestDTO> dtos) {
        return dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
