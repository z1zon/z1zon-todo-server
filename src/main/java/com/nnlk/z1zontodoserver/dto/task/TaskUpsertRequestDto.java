package com.nnlk.z1zontodoserver.dto.task;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.Task;
import com.nnlk.z1zontodoserver.domain.TaskStatus;
import com.nnlk.z1zontodoserver.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TaskUpsertRequestDto {

    @NotNull(message = "name은 필수 값 입니다.")
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

    private TaskStatus taskStatus;

    public Task toEntity(User user, Category category) {

        return Task.builder()
                .content(content)
                .importance(importance)
                .color(color)
                .taskStatus(taskStatus)
                .category(category)
                .user(user)
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }

}
