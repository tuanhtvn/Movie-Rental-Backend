package com.rental.movie.util.mapper;

import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import com.rental.movie.model.entity.Album;
import com.rental.movie.model.entity.Banner;
import com.rental.movie.model.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    @Autowired
    private ModelMapper modelMapper;

    public CategoryResponseDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public List<CategoryResponseDTO> convertToDTO(List<Category> categories) {
        return modelMapper.map(categories, List.class);
    }

    public Category convertToEntity(CategoryRequestDTO dto)
    {
        Category category = modelMapper.map(dto, Category.class);
        // Map Banners
        category.setBanners(dto.getBannersId().stream().map(id -> {
            Banner banner = new Banner();
            banner.setId(id);
            return banner;
        }).collect(Collectors.toList()));
        // Map Albums
        category.setAlbums(dto.getAlbumsId().stream().map(id -> {
            Album album = new Album();
            album.setId(id);
            return album;
        }).collect(Collectors.toList()));
        return category;
    }

    public List<Category> convertToEntity(List<CategoryRequestDTO> dtos) {
        List<Category> categories = dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
        return categories;
    }
}
