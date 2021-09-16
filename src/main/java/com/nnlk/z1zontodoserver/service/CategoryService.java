package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.category.request.CategoryCreateRequestDto;
import com.nnlk.z1zontodoserver.dto.category.request.CategoryUpdateRequestDto;
import com.nnlk.z1zontodoserver.dto.category.response.CategoryResponseDto;
import com.nnlk.z1zontodoserver.exception.NotExistObjectException;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private CategoryRepository categoryRepository;

    public void create(User user, CategoryCreateRequestDto categoryCreateRequestDto) {
        Category category = categoryCreateRequestDto.toEntity(user);
        categoryRepository.save(category);
    }

    @Transactional
    public void update(User user, CategoryUpdateRequestDto categoryUpdateRequestDto, Long cateogoryId) {
        Category category = validateUserCategory(user, cateogoryId);
        category.update(categoryUpdateRequestDto.getCategoryName());
    }
    @Transactional
    public void delete(User user, Long categoryId) {
        validateUserCategory(user, categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryResponseDto> findAll(User user) {

        Long userId = user.getId();
        List<Category> categories = categoryRepository.findAllByUserId(userId);

        return categories.stream()
                .map(cateogory -> cateogory.toResponseDto())
                .collect(Collectors.toList());

    }

    /**
     * 악의적으로 타인의 task 를 삭제할, 갱신 할 수도 있으므로
     * 삭제, 갱신하는 task 가 현재 요청을 보낸 유저의 task 인지를 확인.
     */
    private Category validateUserCategory(User user, Long categoryId) {
        Long userId = user.getId();
        Category category = categoryRepository.findByIdAndUserId(categoryId, userId);

        return Optional.ofNullable(category).orElseThrow(() -> new NotExistObjectException("category is not exist"));
    }
}
