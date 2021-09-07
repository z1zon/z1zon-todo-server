package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.*;
import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CategoryRepository categoryRepository;

    public void createTask(Long userId, TaskCreateDto taskCreateDto) {

        User user = userRepository.findById(userId)
                .orElse(null);

        List<Task> prevTasks = getPrevTasks(taskCreateDto);
        Category category = getCategory(taskCreateDto);

        Task task = Task.upsert(null, user, prevTasks, category, taskCreateDto);

        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(User user, Long taskId, TaskCreateDto taskCreateDto) {
        Task task = validateUserTask(user, taskId);
        List<Task> prevTasks = getPrevTasks(taskCreateDto);
        Category category = getCategory(taskCreateDto);

        Task.upsert(task, null, prevTasks, category, taskCreateDto);
    }

    public void deleteTask(User user, Long taskId) {
        validateUserTask(user, taskId);

        taskRepository.deleteById(taskId);
    }

    /**
     * 악의적으로 타인의 task 를 삭제할, 갱신 할 수도 있으므로
     * 삭제, 갱신하는 task 가 현재 요청을 보낸 유저의 task 인지를 확인.
     */
    private Task validateUserTask(User user, Long taskId) {
        Long userId = user.getId();
        Task task = taskRepository.findByIdAndUserId(taskId, userId);

        return Optional.ofNullable(task).orElseThrow(() -> new RuntimeException());
    }

    private List<Task> getPrevTasks(TaskCreateDto taskCreateDto) {
        return Optional.ofNullable(taskCreateDto.getPrevIds())
                .map(prevTaskIds -> taskRepository.findAllById(prevTaskIds))
                .orElse(null);
    }

    private Category getCategory(TaskCreateDto taskCreateDto) {
        return Optional.ofNullable(taskCreateDto.getCategoryId())
                .map(categoryId -> categoryRepository.findById(categoryId).orElse(null))
                .orElse(null);
    }


}
