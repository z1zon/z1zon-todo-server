package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String content;

    @Column
    private String color;

    @NotNull
    private Integer importance;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTasks = new ArrayList<>();

    /**
     * 하나의 task 는 여러 선행 task 를 가질수 있다.
     * 선후 관계가 존재하는 경우 prevTasks 의 상태가 모두 TaskStatus DONE 일경우에 해당 task 의 상태가 변경가능하다.
     */
    @OneToMany(mappedBy = "next")
    private List<Task> prevTasks = new ArrayList<>();

    /**
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist() {
        this.color = Optional.ofNullable(this.color).orElse("#000000");
    }


    public void setUser(User user) {
        this.user = user;
        user.getTasks().add(this);
    }

    public void addPrevTask(Task prevTask) {
        this.prevTasks.add(prevTask);
    }

    public void addCategory(Category category){
        this.category = category;
    }


}
