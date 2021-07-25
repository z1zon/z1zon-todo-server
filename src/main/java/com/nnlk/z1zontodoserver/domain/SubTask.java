package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class SubTask {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private TaskStatus taskStatus;


}
