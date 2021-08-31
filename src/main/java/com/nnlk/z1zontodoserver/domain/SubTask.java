package com.nnlk.z1zontodoserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private Task task;

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", taskStatus=" + taskStatus +
                ", task=" + task.toString() +
                '}';
    }
}
