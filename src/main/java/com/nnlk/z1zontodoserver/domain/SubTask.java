package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SubTask extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;



}
