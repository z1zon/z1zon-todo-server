package com.nnlk.z1zontodoserver.domain;


import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    private String name;

    private String password;

    private String email;

    private String provider;

    @OneToMany(mappedBy = "member")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Category> categories = new ArrayList<>();

}
