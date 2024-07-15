package com.rental.movie.service;

import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    public List<CategoryResponseDTO> getAllActiveCategories(); //isActive=true && isDeleted=false
    public List<CategoryResponseDTO> getAllSoftDeletedCategories(); //isDeleted=true
    public List<CategoryResponseDTO> getAll(); //get All
    public Page<CategoryResponseDTO> getAll(Pageable pageable, String search);
    public CategoryResponseDTO getCategoryById(String categoryId);
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryDTO);
    public CategoryResponseDTO updateCategory(String categoryId, CategoryRequestDTO categoryDTO);
    public void softDeleteCategory(String categoryId);
    public void restoreCategory(String categoryId);
    public void activateCategory(String categoryId);
    public void deactivateCategory(String categoryId);
}
