package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.*;
import com.nnlk.z1zontodoserver.dto.task.TaskUpsertRequestDto;
import com.nnlk.z1zontodoserver.dto.task.TaskResponseDto;
import com.nnlk.z1zontodoserver.exception.NotExistObjectException;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public void create(User user, TaskUpsertRequestDto taskUpsertRequestDto) {
        Category category = getCategory(taskUpsertRequestDto.getCategoryId());
        Task newTask = taskUpsertRequestDto.toEntity(user, category);

        taskRepository.save(newTask);

    }

    @Transactional
    public void update(User user, Long taskId, TaskUpsertRequestDto taskUpsertRequestDto) {
        Task task = validateUserTask(user, taskId);
        Category category = getCategory(taskUpsertRequestDto.getCategoryId());
        task.update(taskUpsertRequestDto, category);
    }

    @Transactional
    public void delete(User user, Long taskId) {
        validateUserTask(user, taskId);
        taskRepository.deleteById(taskId);
    }

    public List<TaskResponseDto> findAll(User user) {
        Long userId = user.getId();
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream()
                .map(task -> task.toResponseDto())
                .collect(toList());
    }
    /**
     * 악의적으로 타인의 task 를 삭제할, 갱신 할 수도 있으므로
     * 삭제, 갱신하는 task 가 현재 요청을 보낸 유저의 task 인지를 확인.
     */
    private Task validateUserTask(User user, Long taskId) {
        Long userId = user.getId();
        Task task = taskRepository.findByIdAndUserId(taskId, userId);

        return Optional.ofNullable(task).orElseThrow(() -> new NotExistObjectException("task is not exist"));
    }

    private Category getCategory(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(id -> categoryRepository.findById(id).orElse(null))
                .orElse(null);
    }
}
