package com.nnlk.z1zontodoserver.domain;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Category extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "category")
    private List<Task> task = new ArrayList<>();

    // 테스크 보는 페이지
    // 프론트 테스크 줘 !
    // 테스크를주는데(카테고리정보 포함한 테스크를 줘여자)

    // 프론트
    // [task1, 카테(운동)], [task2, 카테(동전)]

}
