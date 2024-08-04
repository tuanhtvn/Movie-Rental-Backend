package com.rental.movie.service;

import com.rental.movie.common.Role;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.CategoryRequestDTO;
import com.rental.movie.model.dto.CategoryResponseDTO;
import com.rental.movie.model.entity.Category;
import com.rental.movie.model.entity.User;
import com.rental.movie.repository.AlbumRepository;
import com.rental.movie.repository.BannerRepository;
import com.rental.movie.repository.CategoryRepository;
import com.rental.movie.util.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserService userService;

    @Override
    public Page<CategoryResponseDTO> getAll(Pageable pageable, String search) {
        Page<Category> categories = categoryRepository.findAll(pageable, search);
        log.info("Get all categories: {}", categories.getContent().toString());
        if(categories.isEmpty()) {
            log.error("No categories found");
            throw new CustomException("Không có danh mục nào", HttpStatus.NOT_FOUND.value());
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public Page<CategoryResponseDTO> getAllActiveCategories(Pageable pageable, String search) {
        Page<Category> categories = categoryRepository.findAllActive(pageable, search);
        log.info("Get all active categories: {}", categories.getContent().toString());
        if(categories.isEmpty()){
            log.error("No active categories found");
            throw new CustomException("Không có danh mục nào", HttpStatus.NOT_FOUND.value());
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public Page<CategoryResponseDTO> getAllInactiveCategories(Pageable pageable, String search) {
        Page<Category> categories = categoryRepository.findAllInActive(pageable, search);
        log.info("Get all inactive categories: {}", categories.getContent().toString());
        if(categories.isEmpty()){
            log.error("No inactive categories found");
            throw new CustomException("Không có danh mục nào", HttpStatus.NOT_FOUND.value());
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public Page<CategoryResponseDTO> getAllSoftDeletedCategories(Pageable pageable, String search) {
        Page<Category> categories = categoryRepository.findAllSoftDelete(pageable, search);
        log.info("Get all soft delete categories: {}", categories.getContent().toString());
        if(categories.isEmpty()){
            log.error("No soft delete categories found");
            throw new CustomException("Không tìm thấy danh mục nào", HttpStatus.NOT_FOUND.value());
        }
        return categoryMapper.convertToDTO(categories);
    }

    @Override
    public CategoryResponseDTO getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found");
                    return new CustomException("Không tìm thấy danh mục", HttpStatus.NOT_FOUND.value());
                });
        if(category.getIsActive().equals(false)) {
            String id = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.getById(id).orElse(null);
            if(user == null || user.getRole().equals(Role.USER)) {
                log.error("User get inactive category");
                throw new CustomException("Bạn không có quyền truy cập danh mục", HttpStatus.METHOD_NOT_ALLOWED.value());
            }
        }
        log.info("Get category by id {}", category.toString());
        return categoryMapper.convertToDTO(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryDTO) {
        Category category = categoryMapper.convertToEntity(categoryDTO);
        log.info("Create category {}", category.toString());
        return categoryMapper.convertToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(
            String categoryId,
            CategoryRequestDTO categoryDTO
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found");
                    return new CustomException("Danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        boolean anyAlumNotFound = categoryDTO.getAlbumsId().stream().anyMatch(
                (albumId) -> !albumRepository.existsById(albumId)
        );
        if(anyAlumNotFound) {
            log.error("Album not found");
            throw new CustomException("Album không tồn tại", HttpStatus.NOT_FOUND.value());
        }
        boolean anyBannerNotFound = categoryDTO.getBannersId().stream().anyMatch(
                (bannerId) -> !bannerRepository.existsById(bannerId)
        );
        if(anyBannerNotFound) {
            log.error("Banner not found");
            throw new CustomException("Banner không tồn tại", HttpStatus.NOT_FOUND.value());
        }
        log.info("Update category {}", category.toString());
        category = categoryMapper.convertToEntity(categoryDTO, category);
        return categoryMapper.convertToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void softDeleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found");
                    return new CustomException("ID danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        category.setIsDeleted(true);
        categoryRepository.save(category);
        log.info("Soft delete category {}", category.toString());
    }

    @Override
    @Transactional
    public void restoreCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found");
                    return new CustomException("ID danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        category.setIsDeleted(false);
        categoryRepository.save(category);
        log.info("Restore category {}", category.toString());
    }

//    @Override
//    @Transactional
//    public void activateCategory(String categoryId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> {
//                    log.error("Category not found");
//                    return new CustomException("ID danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
//                });
//        category.setIsActive(true);
//        categoryRepository.save(category);
//        log.info("Activate category {}", category.toString());
//    }
//
//    @Override
//    @Transactional
//    public void deactivateCategory(String categoryId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> {
//                    log.error("Category not found");
//                    return new CustomException("ID danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
//                });
//        category.setIsActive(false);
//        categoryRepository.save(category);
//        log.info("Deactivate category {}", category.toString());
//    }

    @Override
    @Transactional
    public boolean toggleActiveStatus(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.error("Category not found");
                    return new CustomException("ID danh mục không tồn tại", HttpStatus.NOT_FOUND.value());
                });
        category.setIsActive(!category.getIsActive());
        categoryRepository.save(category);
        log.info("Deactivate category {}", category.toString());
        return category.getIsActive();
    }
}
