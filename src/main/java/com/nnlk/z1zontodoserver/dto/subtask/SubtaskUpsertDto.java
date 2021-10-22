package com.nnlk.z1zontodoserver.dto.subtask;

import com.nnlk.z1zontodoserver.domain.SubTask;
import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class SubtaskUpsertDto {
    @NotNull(message = "content은 필수 값 입니다.")
    private String content;

    private TaskStatus taskStatus;

    private Long taskId;

    public SubTask toEntity(Task task) {
        return SubTask.builder()
                .content(content)
                .taskStatus(taskStatus)
                .task(task)
                .build();
    }

}
