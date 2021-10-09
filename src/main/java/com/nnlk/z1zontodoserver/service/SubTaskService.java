package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.SubTask;
import com.nnlk.z1zontodoserver.domain.Task;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.subtask.SubTaskUpsertDto;
import com.nnlk.z1zontodoserver.exception.NotExistObjectException;
import com.nnlk.z1zontodoserver.repository.SubTaskRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    public void create(User user, SubTaskUpsertDto subTaskUpsertDto) {
        Task task = validateUserTask(user, subTaskUpsertDto.getTaskId());
        SubTask newSubTask = subTaskUpsertDto.toEntity(task);
        subTaskRepository.save(newSubTask);
    }

    /**
     * 중복되는 코드, 유틸폴더로 분리 필요
     */
    private Task validateUserTask(User user, Long taskId) {
        Long userId = user.getId();
        Task task = taskRepository.findByIdAndUserId(taskId, userId);

        return Optional.ofNullable(task).orElseThrow(() -> new NotExistObjectException("task is not exist"));
    }

}
