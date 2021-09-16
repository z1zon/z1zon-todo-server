package com.nnlk.z1zontodoserver.dto.category.request;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryUpdateRequestDto {

    @NotNull
    String categoryName;

    public Category toEntity(User user){
        return Category.builder()
                .name(categoryName)
                .user(user)
                .build();
    }
}
