package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.subtask.SubtaskResponseDto;
import com.nnlk.z1zontodoserver.dto.subtask.SubtaskUpsertDto;

import lombok.*;
import javax.persistence.*;
import java.util.Optional;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class SubTask extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    @PrePersist
    public void perPersist() {
        this.taskStatus = Optional.ofNullable(this.taskStatus).orElse(TaskStatus.TODO);
    }

    public void update(SubtaskUpsertDto subTaskUpsertDto) {
        System.out.println(subTaskUpsertDto.getTaskStatus());
        this.content = subTaskUpsertDto.getContent();
        this.taskStatus = subTaskUpsertDto.getTaskStatus();
    }

    public SubtaskResponseDto toResponseDto() {
        return SubtaskResponseDto.builder()
                .id(id)
                .content(content)
                .taskStatus(taskStatus)
                .build();
    }

}
