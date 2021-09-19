package com.nnlk.z1zontodoserver.controller;


import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.category.request.CategoryUpsertRequestDto;
import com.nnlk.z1zontodoserver.dto.category.response.CategoryResponseDto;
import com.nnlk.z1zontodoserver.dto.common.ResponseDto;
import com.nnlk.z1zontodoserver.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    private ResponseDto create(@ApiIgnore @AuthenticationPrincipal User user,
                               @RequestBody @Valid CategoryUpsertRequestDto categoryUpsertRequestDto) {
        categoryService.create(user, categoryUpsertRequestDto);

        return ResponseDto.builder()
                .status(HttpStatus.CREATED)
                .messsage("category create success")
                .build();
    }

    @GetMapping("/categories")
    private ResponseDto findAll(@AuthenticationPrincipal User user) {
        List<CategoryResponseDto> categoryResponseDtoList = categoryService.findAll(user);

        return ResponseDto.builder()
                .messsage("categories lookup success")
                .status(HttpStatus.OK)
                .data(categoryResponseDtoList)
                .build();
    }

    @PostMapping("/category/update/{categoryId}")
    public ResponseDto update(@AuthenticationPrincipal User user,
                              @Valid @RequestBody CategoryUpsertRequestDto categoryUpsertRequestDto,
                              @PathVariable Long categoryId) {
        categoryService.update(user, categoryUpsertRequestDto, categoryId);
        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long categoryId) {
        categoryService.delete(user, categoryId);

        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }


}