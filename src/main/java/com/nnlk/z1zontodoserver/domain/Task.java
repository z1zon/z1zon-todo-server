package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.task.TaskResponseDto;
import com.nnlk.z1zontodoserver.dto.task.TaskUpsertRequestDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    @Column
    private String color;

    private Integer importance;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private LocalDate startAt;

    private LocalDate endAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist() {
        this.color = Optional.ofNullable(this.color).orElse("#000000");
        this.importance = Optional.ofNullable(this.importance).orElse(3);
    }

    public void deleteUser() {
        this.user = null;
        this.category = null;
    }

    public void deleteCategory(){
        this.category = null;
    }

    public TaskResponseDto toResponseDto() {
        return TaskResponseDto.builder()
                .id(this.id)
                .category(this.category) // 해당 부분 categoryDto 변환 필요, merge 후 작업
                .color(this.color)
                .importance(this.importance)
                .taskStatus(this.taskStatus)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .userId(this.user.getId())
                .build();
    }

    public void update(TaskUpsertRequestDto taskUpsertRequestDto, Category category) {
        this.content = taskUpsertRequestDto.getContent();
        this.color = taskUpsertRequestDto.getColor();
        this.importance = taskUpsertRequestDto.getImportance();
        this.taskStatus = taskUpsertRequestDto.getTaskStatus();
        this.startAt = taskUpsertRequestDto.getStartAt();
        this.endAt = taskUpsertRequestDto.getEndAt();
        this.category = category;
    }

}
