package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private String color;

    private Integer importance;

    private TaskStatus taskStatus;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

}
