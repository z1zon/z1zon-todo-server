package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Category extends BaseTime{

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;


}
