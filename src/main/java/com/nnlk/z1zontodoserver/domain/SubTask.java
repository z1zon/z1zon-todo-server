package com.nnlk.z1zontodoserver.domain;

import lombok.*;

import javax.persistence.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@ToString
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

}
