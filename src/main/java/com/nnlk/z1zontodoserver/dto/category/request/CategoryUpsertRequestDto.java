package com.nnlk.z1zontodoserver.dto.category.request;

import com.nnlk.z1zontodoserver.domain.Category;
import com.nnlk.z1zontodoserver.domain.User;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor //없으면 deserialization 안된다.
public class CategoryUpsertRequestDto {
    @NotNull
    private String categoryName;

    public Category toEntity(User user){
        return Category.builder()
                .name(categoryName)
                .user(user)
                .build();
    }
}
