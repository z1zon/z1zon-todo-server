package com.nnlk.z1zontodoserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@Getter
public class Task extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    @ColumnDefault("#000000")
    private String color;

    private Integer importance;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTasks = new ArrayList<>();


}
