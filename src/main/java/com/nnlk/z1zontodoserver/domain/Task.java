package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.task.TaskCreateDto;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Getter
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

    private LocalDate startAt;

    private LocalDate endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    // task.getCategoey

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTasks = new ArrayList<>();

    /**
     * 하나의 task 는 여러 선행 task 를 가질수 있다.
     * 선후 관계가 존재하는 경우 prevTasks 의 상태가 모두 TaskStatus DONE 일경우에 해당 task 의 상태가 변경가능하다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "after_id")
    private Task afterTask;

    @OneToMany(mappedBy = "afterTask")
    private List<Task> prevTasks = new ArrayList<>();

    /**
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist() {
        this.color = Optional.ofNullable(this.color).orElse("#000000");
        this.importance = Optional.ofNullable(this.importance).orElse(3);
    }

    public void setUser(User user) {
        this.user = user;
        user.getTasks().add(this);
    }

    // 문제생긴다.
    // 1 task , category 2,  => category [task1]
    // 1 task , category 3   => category [task1. task1]
    public void setCategory(Category category) {
        this.category = category;
        category.getTask().add(this);
    }

    public void addPrevTask(Task prevTask) {
        prevTask.afterTask = this;
        this.prevTasks.add(prevTask);
    }

    public static Task upsert(Task existTask, User user, List<Task> prevTasks, Category category, TaskCreateDto taskCreateDto) {

        Task task = Optional
                .ofNullable(existTask)
                .orElse(new Task());

        // 초기 생성인경우에만 task 의 user 를 세팅해준다.
        if (user != null) {
            task.setUser(user);
        }

        Optional.ofNullable(category).ifPresent(addedCategory -> {
            task.setCategory(addedCategory);
        });

        prevTasks.stream().forEach(prevTask -> {
            task.addPrevTask(prevTask);
        });

        task.prevTasks = prevTasks;
        task.startAt = taskCreateDto.getStartAt();
        task.endAt = taskCreateDto.getEndAt();
        task.importance = taskCreateDto.getImportance();
        task.content = taskCreateDto.getContent();
        task.taskStatus = TaskStatus.TODO;

        return task;
    }


}
