package com.nnlk.z1zontodoserver.repositoryTest;

import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
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
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    public void deleteAll(){
        taskRepository.deleteAll();
    }

    @DisplayName("Task 생성시 JPA Auditing 생성시간 추가 확인")
    @Test
    public void auditCreatedDate(){
        LocalDateTime localDateTime = LocalDateTime.now();

        testEntityManager.persist(
                Task.builder()
                .content("content")
                .endAt(localDateTime)
                .startAt(localDateTime)
                .importance(Integer.valueOf(10))
                .build());

        Task task = taskRepository.findById(1L).orElse(null);
        Assert.assertTrue(task.getCreatedAt().isAfter(localDateTime));
        Assert.assertTrue(task.getUpdatedAt().isAfter(localDateTime));
        Assert.assertTrue("#000000".equals(task.getColor()));

    }
}
