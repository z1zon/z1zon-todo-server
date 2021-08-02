package com.nnlk.z1zontodoserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist(){
        this.color = Optional.ofNullable(this.color).orElse("#000000");
    }

}
