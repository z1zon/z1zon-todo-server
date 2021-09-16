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
        User user = User.builder()
                .name("test-user")
                .email("test@naver.com")
                .password("1234")
                .build();
        userRepository.save(user);
        categoryRepository.save(Category.builder()
                                .name("카테고리")
                                .user(user)
                                .build());
        categoryRepository.save(Category.builder()
                .name("카테고리2")
                .user(user)
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

        Category 카테고리 = categoryRepository.save(category);
        User 카테고리_유저 = 카테고리.getUser();
        Assert.assertTrue("test-user".equals(카테고리_유저.getName()));
        Assert.assertTrue("test-category".equals(카테고리.getName()));

    }
    @DisplayName("카테고리 조회 테스트")
    @Test
    public void findAll(){
        assert (categoryRepository.findAll().size() ==2);
        assert (categoryRepository.findAll().stream().filter(category ->
                "test@naver.com".equals(category.getUser().getEmail())).count() ==2);
    }
    @DisplayName("카테고리 업데이트 테스트")
    @Test
    public void update(){
        categoryRepository.findAll().stream().forEach(category -> category.update("1234"));
        categoryRepository.flush();
        assert(categoryRepository.findAll().stream().allMatch(category -> "1234".equals(category.getName())));
    }

    @DisplayName("카테고리 삭제 테스트")
    @Test
    public void delete(){
        categoryRepository.findAll().stream().forEach(categoryRepository::delete);
        assert(categoryRepository.findAll().size()==0);
    }

}
