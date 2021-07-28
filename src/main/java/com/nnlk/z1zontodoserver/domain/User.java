package com.nnlk.z1zontodoserver.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 128, nullable = false)
    private String name;

    private String password;

    @NotNull
    private String email;

    private String provider;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<>();

}
