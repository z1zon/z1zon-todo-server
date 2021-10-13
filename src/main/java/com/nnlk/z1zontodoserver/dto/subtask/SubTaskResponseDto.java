package com.nnlk.z1zontodoserver.dto.subtask;

import com.nnlk.z1zontodoserver.domain.TaskStatus;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SubTaskResponseDto {
    private Long id;

    private String content;

    private TaskStatus taskStatus;
}
