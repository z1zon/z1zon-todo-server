package com.nnlk.z1zontodoserver.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

    private LocalDate startAt;

    private LocalDate endAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * insert 되기전(persist 되기 전) 실행된다.
     */
    @PrePersist
    public void perPersist() {
        this.color = Optional.ofNullable(this.color).orElse("#000000");
        this.importance = Optional.ofNullable(this.importance).orElse(3);
    }

}
