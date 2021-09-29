package com.nnlk.z1zontodoserver.serviceTest;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @BeforeEach
    public void createTestUser() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        User user = User.builder()
                .name("test-user")
                .email("test@naver.com")
                .password("1234")
                .build();
        Category category = Category.builder()
                .name("test-category")
                .user(user)
                .build();
        Task task = Task.builder()
                .content("content")
                .endAt(localDate)
                .startAt(localDate)
                .importance(Integer.valueOf(10))
                .user(user)
                .build();

        userRepository.save(user);
        categoryRepository.save(category);
        taskRepository.save(task);
    }


    @Test
    @Transactional
    public void update() {
        UserUpsertRequestDto userUpsertRequestDto = new UserUpsertRequestDto("test-user-update", "1234", "role", "test@naver.com","provider");
        User user = Optional.of(userRepository.findByEmail("test@naver.com")).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        user.update(userUpsertRequestDto);
        userRepository.flush();
        User updatedUser = Optional.of(userRepository.findByEmail("test@naver.com")).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        assert ("test-user-update".equals(taskRepository.findAllByUserId(updatedUser.getId()).get(0).getUser().getName()));
        assert ("test-user-update".equals(updatedUser.getName()));
    }

    @Test
    @Transactional
    public void delete() {
        User user = userRepository.findByEmail("test@naver.com");
        categoryRepository.findAllByUserId(user.getId()).forEach(category -> category.deleteUser());
        taskRepository.findAllByUserId(user.getId()).forEach(task -> task.deleteUser());
        userRepository.delete(user);

        assert (userRepository.findByEmail("test@naver.com") ==  null);
    }

}
