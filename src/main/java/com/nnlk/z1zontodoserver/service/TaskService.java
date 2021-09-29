package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.*;
import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import com.nnlk.z1zontodoserver.exception.NotExistObjectException;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public void create(User user, TaskCreateDto taskCreateDto) {

        Category category = Optional.ofNullable(taskCreateDto.getCategoryId())
                .map(catetoryId -> categoryRepository.findById(catetoryId).orElse(null))
                .orElse(null);

        Task newTask = taskCreateDto.toEntity(user, category);

        taskRepository.save(newTask);

    }


    @Transactional
    public void update(User user, Long taskId, TaskCreateDto taskCreateDto) {
        Task task = validateUserTask(user, taskId);


    }

    public void deleteTask(User user, Long taskId) {
        validateUserTask(user, taskId);
        taskRepository.deleteById(taskId);
    }

    public List<Task> findAll(User user) {

        Long userId = user.getId();
        List<Task> tasks = taskRepository.findAllByUserId(userId);

        return Optional.ofNullable(tasks).orElse(new ArrayList<>());

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


//    private List<Task> getPrevTasks(TaskCreateDto taskCreateDto) {
//        return Optional.ofNullable(taskCreateDto.getPrevIds())
//                .map(prevTaskIds -> taskRepository.findAllById(prevTaskIds))
//                .orElse(null);
//    }
//
//    private Category getCategory(TaskCreateDto taskCreateDto) {
//        return Optional.ofNullable(taskCreateDto.getCategoryId())
//                .map(categoryId -> categoryRepository.findById(categoryId).orElse(null))
//                .orElse(null);
//    }


}
