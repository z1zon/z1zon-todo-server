package com.nnlk.z1zontodoserver.dto.task;

import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class TaskCreateDto {

    @NotNull(message = "content 값은 필수 값 입니다.")
    private String content;

    @NotNull(message = "startAt 값은 필수 값 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @NotNull(message = "endAt 값은 필수 값 입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    private String color;

    private Integer importance;

    private Long categoryId;

    private List<Long> prevIds;

    private TaskStatus taskStatus;


}
