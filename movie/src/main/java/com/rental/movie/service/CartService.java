package com.rental.movie.service;

import com.rental.movie.model.dto.CartRequestDTO;
import com.rental.movie.model.dto.CartResponseDTO;

public interface CartService {
    void addToCart(CartRequestDTO cartRequestDTO);
    CartResponseDTO viewCart();
    void removeFromCart(String filmId);
}
