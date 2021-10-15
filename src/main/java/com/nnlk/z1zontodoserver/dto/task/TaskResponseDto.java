package com.nnlk.z1zontodoserver.dto.task;

import com.nnlk.z1zontodoserver.domain.TaskStatus;
import com.nnlk.z1zontodoserver.dto.category.response.CategoryResponseDto;
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

    private CategoryResponseDto category;

    private Long userId;
}
