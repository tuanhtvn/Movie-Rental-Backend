package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.BannerRequestDTO;
import com.rental.movie.model.dto.BannerResponseDTO;
import com.rental.movie.model.dto.FilmRequestDTO;
import com.rental.movie.model.dto.FilmResponseDTO;
import com.rental.movie.model.entity.Banner;
import com.rental.movie.model.entity.Film;
import com.rental.movie.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmMapper filmMapper;

    public BannerResponseDTO convertToDTO(Banner banner) {
        if (banner == null) {
            return null;
        }
        BannerResponseDTO responseBanner = new BannerResponseDTO();
        if (banner.getFilm() != null) {
            Film film = filmRepository.findByIdDefault(banner.getFilm().getId()).orElse(null);
            if (film != null) {
                FilmResponseDTO responseFilm = filmMapper.convertToDTO(film);
                responseBanner.setFilm(responseFilm);
            }
        }
        responseBanner.setId(banner.getId());
        responseBanner.setImageUrl(banner.getImageUrl());
        responseBanner.setIsActive(banner.getIsActive());
        responseBanner.setIsDeleted(banner.getIsDeleted());
        responseBanner.setCreatedAt(banner.getCreatedAt().toInstant());
        responseBanner.setUpdatedAt(banner.getUpdatedAt().toInstant());

        return responseBanner;
    }


    public Banner convertToEntity(BannerRequestDTO bannerRequestDTO){
        if (bannerRequestDTO == null) {
            return null;
        }
        Film film = filmRepository.findByIdDefault(bannerRequestDTO.getIdFilm()).orElse(null);

        Banner banner = new Banner();
        banner.setImageUrl(bannerRequestDTO.getImageUrl());
        banner.setFilm(film);
        banner.setIsActive(bannerRequestDTO.getIsActive());
        banner.setIsDeleted(bannerRequestDTO.getIsDeleted());

        return banner;
    }
}
