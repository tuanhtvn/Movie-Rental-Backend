package com.rental.movie.service;

import com.rental.movie.common.IAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.rental.movie.exception.NotFoundException;
import com.rental.movie.model.dto.CartRequestDTO;
import com.rental.movie.model.dto.CartResponseDTO;
import com.rental.movie.model.entity.Item;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.FilmRepository;
import com.rental.movie.repository.UserRepository;
import com.rental.movie.util.mapper.CartMapper;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilmService filmService;
    @Autowired
    private CartMapper cartMapper;
    @Lazy
    @Autowired
    private IAuthentication authManager;

    @Override
    public void addToCart(String filmId) {
        User user = authManager.getUserAuthentication();
        Item item = new Item();
        item.setFilm(filmService.getById(filmId));
        user.getCart().add(item);
        userRepository.save(user);
    }

    @Override
    public CartResponseDTO viewCart() {
        User user = authManager.getUserAuthentication();
        return cartMapper.convertToDTO(user);
    }

    @Override
    public void removeFromCart(String filmId) {
        User user = authManager.getUserAuthentication();
        Item itemToRemove = user.getCart().stream()
                .filter(item -> item.getFilm().getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phim trong giỏ hàng"));

        user.getCart().remove(itemToRemove);
        userRepository.save(user);
    }
}
