package com.rental.movie.util.mapper;

import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import com.rental.movie.model.entity.Category;
import com.rental.movie.repository.AlbumRepository;
import com.rental.movie.repository.BannerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private AlbumRepository albumRepository;

    public CategoryResponseDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public Page<CategoryResponseDTO> convertToDTO(Page<Category> categories) {
        return categories.map(this::convertToDTO);
    }

    public Category convertToEntity(CategoryRequestDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        // Map Banners
        category.setBanners(categoryDTO.getBannersId().stream().map(id -> {
            return bannerRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy banner", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Albums
        category.setAlbums(categoryDTO.getAlbumsId().stream().map(id -> {
            return albumRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Album", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return category;
    }

    // Overloading for update category
    public Category convertToEntity(CategoryRequestDTO dto, Category category) {
        modelMapper.map(dto, category);
        // Map Banners
        category.setBanners(dto.getBannersId().stream().map(id -> {
            return bannerRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy banner", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        // Map Albums
        category.setAlbums(dto.getAlbumsId().stream().map(id -> {
            return albumRepository.findById(id).orElseThrow(
                    () -> new CustomException("Không tìm thấy Album", HttpStatus.NOT_FOUND.value()));
        }).collect(Collectors.toList()));
        return category;
    }
}
