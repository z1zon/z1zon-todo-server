package com.nnlk.z1zontodoserver.dto.task;

import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TaskCreateDto {

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private TaskStatus status;

    private String color;

    private Integer importance;

    private Integer categoryId;

    private List<Long> prevIds;


}
