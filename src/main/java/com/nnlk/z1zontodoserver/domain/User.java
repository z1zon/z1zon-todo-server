package com.nnlk.z1zontodoserver.domain;


import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 128, nullable = false)
    private String name;

    private String password;

    @NotNull
    private String email;

    @ColumnDefault("local")
    private String provider;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany()
    private List<Category> categories = new ArrayList<>();

}
