package com.nnlk.z1zontodoserver.repositoryTest;

import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void deleteAll(){
        userRepository.deleteAll();
    }

    @DisplayName("User 생성시 JPA Auditing 생성시간 및 default 값 추가 확인")
    @Test
    public void auditCreatedDate(){
        LocalDateTime localDateTime = LocalDateTime.now();

        testEntityManager.persist(
                User.builder()
                    .name("pyu")
                    .email("1234")
                    .password("1111")
                    .build()
        );

        User user = userRepository.findById(1L).orElse(null);
        Assert.assertTrue(user.getCreatedAt().isAfter(localDateTime));
        Assert.assertTrue(user.getUpdatedAt().isAfter(localDateTime));
        Assert.assertTrue("local".equals(user.getProvider()));

    }

}
