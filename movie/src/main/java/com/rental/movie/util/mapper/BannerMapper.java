package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.BannerCreationDTO;
import com.rental.movie.model.dto.BannerDTO;
import com.rental.movie.model.entity.Banner;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FilmRepository filmRepository;

    public BannerDTO convertToDTO(Banner banner){
        if (banner == null) {
            return null;
        }
        return new BannerDTO(
                banner.getId(),
                banner.getImageUrl(),
                banner.getFilm(),
                banner.getIsActive(),
                banner.getIsDeleted(),
                banner.getCreatedAt(),
                banner.getUpdatedAt()
        );
    }

    public Banner convertToEntity(BannerCreationDTO bannerCreationDTO){
        if (bannerCreationDTO == null) {
            return null;
        }
        Film film = filmRepository.findById(bannerCreationDTO.getIdFilm()).orElse(null);

        Banner banner = new Banner();
        banner.setImageUrl(bannerCreationDTO.getImageUrl());
        banner.setFilm(film);
        banner.setIsActive(bannerCreationDTO.getIsActive());
        banner.setIsDeleted(bannerCreationDTO.getIsDeleted());

        return banner;
    }
}
