package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Category extends BaseTime{

    @Id
    @GeneratedValue
    private Long id;

    private String name;


}
