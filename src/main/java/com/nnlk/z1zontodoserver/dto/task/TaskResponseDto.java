package com.nnlk.z1zontodoserver.dto.task;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TaskResponseDto {
    private Long id;

    private String content;

    private String color;

    private Integer importance;

    private TaskStatus taskStatus;

    private LocalDate startAt;

    private LocalDate endAt;

    private Category category;

    private Long userId;
}
