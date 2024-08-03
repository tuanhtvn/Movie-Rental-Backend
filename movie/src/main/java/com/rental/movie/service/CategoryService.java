package com.rental.movie.service;

import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    public Page<CategoryResponseDTO> getAllActiveCategories(Pageable pageable, String search); //isActive=true && isDeleted=false
    public Page<CategoryResponseDTO> getAllInactiveCategories(Pageable pageable, String search); //isActive=false && isDeleted=false
    public Page<CategoryResponseDTO> getAllSoftDeletedCategories(Pageable pageable, String search); //isDeleted=true
    public Page<CategoryResponseDTO> getAll(Pageable pageable, String search);
    public CategoryResponseDTO getCategoryById(String categoryId);
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryDTO);
    public CategoryResponseDTO updateCategory(String categoryId, CategoryRequestDTO categoryDTO);
    public void softDeleteCategory(String categoryId);
    public void restoreCategory(String categoryId);
//    public void activateCategory(String categoryId);
//    public void deactivateCategory(String categoryId);
    public boolean toggleActiveStatus(String categoryId);
}
