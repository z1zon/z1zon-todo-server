package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.subtask.SubTaskUpsertDto;
import com.nnlk.z1zontodoserver.dto.task.TaskUpsertRequestDto;
import lombok.*;

import javax.persistence.*;


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

    public void update(SubTaskUpsertDto subTaskUpsertDto) {
        this.content = subTaskUpsertDto.getContent();
        this.taskStatus = subTaskUpsertDto.getTaskStatus();
    }

}