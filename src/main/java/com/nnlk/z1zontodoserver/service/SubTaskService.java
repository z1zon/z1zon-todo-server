package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.SubTask;
import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.subtask.SubtaskResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubtaskUpsertDto;
import com.nnlk.z1zontodoserver.exception.NotExistObjectException;
import com.nnlk.z1zontodoserver.repository.SubTaskRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    public void create(User user, SubtaskUpsertDto subTaskUpsertDto) {
        Task task = validateUserTask(user, subTaskUpsertDto.getTaskId());
        SubTask newSubTask = subTaskUpsertDto.toEntity(task);
        subTaskRepository.save(newSubTask);
    }

    @Transactional
    public void delete(User user, Long subtaskId) {
        SubTask subTask = validateSubtask(subtaskId);
        Long taskId = subTask.getTask().getId();
        validateUserTask(user, taskId);

        subTaskRepository.deleteById(subtaskId);
    }

    @Transactional
    public void update(User user, Long subtaskId, SubtaskUpsertDto subTaskUpsertDto) {
        validateUserTask(user, subTaskUpsertDto.getTaskId());
        SubTask subTask = validateSubtask(subtaskId);
        subTask.update(subTaskUpsertDto);
    }

    public List<SubtaskResponseDto> findAll(User user, Long taskId) {
        validateUserTask(user, taskId);
        List<SubTask> subTasks = subTaskRepository.findAllByTaskId(taskId);

        return subTasks.stream()
                .map(subTask -> subTask.toResponseDto())
                .collect(toList());
    }

    private SubTask validateSubtask(Long subtaskId) {
        return Optional.ofNullable(subTaskRepository.findById(subtaskId).get())
                .orElseThrow(() -> new NotExistObjectException("subtask is not exist"));
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
