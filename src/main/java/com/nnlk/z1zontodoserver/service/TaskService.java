package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.*;
import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public void createTask(Long userId, TaskCreateDto taskCreateDto) {

        User user = userRepository.findById(userId).get();

        // 여기서 User 연관관계 설정해주고, User 객체에 필요한 값 세팅 해준다.
        Task task = new Task();

        // task 와 관련된 연관 관계 설정.
        Category category = task.getCategory();
        List<Long> prevTaskIds = taskCreateDto.getPrevIds();
        List<Task> prevTasks = taskRepository.findAllById(prevTaskIds);

        task.setUser(user);
        task.addCategory(category);
        prevTasks.stream().forEach(prevTask -> task.addPrevTask(task));
        
        // task 의 값 세팅.
        task.setTaskStatus(TaskStatus.TODO);
        task.setStartAt(taskCreateDto.getStartAt()); // not null
        task.setEndAt(taskCreateDto.getEndAt()); // not null
        task.setColor(taskCreateDto.getColor()); // nullable
        task.setImportance(taskCreateDto.getImportance()); // not null

        // User 객체 저장해준다.
        taskRepository.save(task);

        // 리턴해준다.

    }

}
