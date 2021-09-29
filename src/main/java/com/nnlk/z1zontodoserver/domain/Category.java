package com.nnlk.z1zontodoserver.domain;

import com.nnlk.z1zontodoserver.dto.category.response.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Category extends BaseTime {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public CategoryResponseDto toResponseDto() {
        return CategoryResponseDto.builder()
                .id(this.id)
                .categoryName(this.name)
                .userId(this.user.getId())
                .updatedAt(this.getUpdatedAt().toString())
                .createdAt(this.getCreatedAt().toString())
                .build();
    }

    public void update(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.user = null;
    }

}
