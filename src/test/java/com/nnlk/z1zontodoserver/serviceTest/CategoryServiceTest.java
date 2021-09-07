package com.nnlk.z1zontodoserver.serviceTest;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void deleteAll() {
        categoryRepository.deleteAll();

    }

    @BeforeEach
    public void createTestUser() {
        userRepository.save(User.builder()
                .name("test-user")
                .email("test@naver.com")
                .password("1234")
                .build());
    }

    @DisplayName("카테고리 생성 테스트")
    @Test
    public void createCategoryRepository() {
        User user = userRepository.findByEmail("test@naver.com");

        Category category = Category.builder()
                .name("test-category")
                .user(user)
                .build();
        List<Category> categoryList = Optional.ofNullable(user.getCategories()).orElse(new ArrayList<>());
        categoryList.add(category);
        user.addCategory(categoryList);
        Category 카테고리 = categoryRepository.save(category);
        User 카테고리_유저 = 카테고리.getUser();
        Assert.assertTrue("test-user".equals(카테고리_유저.getName()));
        Assert.assertTrue("test-user".equals(카테고리_유저.getName()));
        Assert.assertTrue("test-category".equals(카테고리.getName()));

    }
}
