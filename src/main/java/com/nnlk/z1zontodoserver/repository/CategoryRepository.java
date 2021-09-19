package com.nnlk.z1zontodoserver.repository;

import com.nnlk.z1zontodoserver.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByIdAndUserId(Long categoryId, Long userId);
    List<Category> findAllByUserId(Long userId);

}
