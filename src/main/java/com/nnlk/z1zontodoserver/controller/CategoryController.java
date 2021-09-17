package com.nnlk.z1zontodoserver.controller;


import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.category.request.CategoryCreateRequestDto;
import com.nnlk.z1zontodoserver.dto.category.request.CategoryUpdateRequestDto;
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
                               @RequestBody @Valid CategoryCreateRequestDto categoryCreateRequestDto) {
        categoryService.create(user, categoryCreateRequestDto);

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

    @PostMapping("/category/update/{cateogoryId}")
    public ResponseDto update(@AuthenticationPrincipal User user,
                              @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto,
                              @PathVariable Long cateogoryId){
        categoryService.update(user, categoryUpdateRequestDto,cateogoryId);
        return ResponseDto.builder()
                .messsage("update success")
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/category/{cateogoryId}")
    public ResponseDto delete(@ApiIgnore @AuthenticationPrincipal User user, @PathVariable Long cateogoryId) {
        categoryService.delete(user, cateogoryId);

        return ResponseDto.builder()
                .messsage("delete success")
                .status(HttpStatus.OK)
                .build();
    }


}