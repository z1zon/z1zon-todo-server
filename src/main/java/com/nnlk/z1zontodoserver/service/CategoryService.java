package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
}
