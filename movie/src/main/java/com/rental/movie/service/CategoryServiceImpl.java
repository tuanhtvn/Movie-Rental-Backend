package com.rental.movie.service;

import com.rental.movie.exception.NotFoundException;
import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import com.rental.movie.model.entity.Category;
import com.rental.movie.repository.AlbumRepository;
import com.rental.movie.repository.BannerRepository;
import com.rental.movie.repository.CategoryRepository;
import com.rental.movie.util.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> getAllActiveCategories() {
        List<Category> categories = categoryRepository.findByIsDeletedFalseAndIsActiveTrue();
        if(categories.isEmpty()){
            throw new NotFoundException("Không có danh mục nào");
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public List<CategoryResponseDTO> getAllSoftDeletedCategories() {
        List<Category> categories = categoryRepository.findByIsDeletedTrue();
        if(categories.isEmpty()){
            throw new NotFoundException("Không tìm thấy danh mục nào");
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new NotFoundException("Không có danh mục nào");
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public Page<CategoryResponseDTO> getAll(Pageable pageable, String search) {
        Page<CategoryResponseDTO> categories = categoryRepository.findContaining(pageable, search).map(categoryMapper::convertToDTO);
        if(categories.isEmpty()) {
            throw new NotFoundException("Không có danh mục nào");
        }
        return categories;
    }

    @Override
    public CategoryResponseDTO getCategoryById(String categoryId) {
        return categoryMapper.convertToDTO(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục")));
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryDTO) {
        Category category = categoryMapper.convertToEntity(categoryDTO);
        return categoryMapper.convertToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(
            String categoryId,
            CategoryRequestDTO categoryDTO
    ) {
        if(categoryId == null || !categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Danh mục không tồn tại");
        }
        boolean anyAlumNotFound = categoryDTO.getAlbumsId().stream().anyMatch(
                (albumId) -> !albumRepository.existsById(albumId)
        );
        if(anyAlumNotFound) {
            throw new NotFoundException("Album không tồn tại");
        }
        boolean anyBannerNotFound = categoryDTO.getBannersId().stream().anyMatch(
                (bannerId) -> !bannerRepository.existsById(bannerId)
        );
        if(anyBannerNotFound) {
            throw new NotFoundException("Banner không tồn tại");
        }
        categoryDTO.setId(categoryId);
        Category category = categoryMapper.convertToEntity(categoryDTO);
        System.out.println(category.getBanners());
        return categoryMapper.convertToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void softDeleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("ID danh mục không tồn tại")
        );
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void restoreCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("ID danh mục không tồn tại")
        );
        category.setIsDeleted(false);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void activateCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("ID danh mục không tồn tại")
        );
        category.setIsActive(true);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deactivateCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException("ID danh mục không tồn tại")
        );
        category.setIsActive(false);
        categoryRepository.save(category);
    }
}
