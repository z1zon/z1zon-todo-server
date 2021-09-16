package com.nnlk.z1zontodoserver.dto.category.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponseDto {

    private String categoryName;

    private Long id;

    private String name;

    private Long userId;

    private String createdAt;

    private String updatedAt;

}
