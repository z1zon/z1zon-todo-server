package com.nnlk.z1zontodoserver.service;

import com.nnlk.z1zontodoserver.domain.User;
import com.nnlk.z1zontodoserver.dto.user.request.UserUpsertRequestDto;
import com.nnlk.z1zontodoserver.repository.CategoryRepository;
import com.nnlk.z1zontodoserver.repository.TaskRepository;
import com.nnlk.z1zontodoserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    @Transactional
    public void update(User user, UserUpsertRequestDto userUpsertRequestDto, Long userId) throws InvalidKeyException {
        validateUser(user, userId);
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        userUpsertRequestDto.setEncryptPassword(authService.getSHA256Pwd(updateUser.getPassword()));
        updateUser.update(userUpsertRequestDto);
    }

    @Transactional
    public void delete(User user, Long userId) throws InvalidKeyException {
        validateUser(user, userId);
        taskRepository.findAllByUserId(userId).forEach(task -> task.deleteUser());
        categoryRepository.findAllByUserId(userId).forEach(category -> category.deleteUser());
        userRepository.deleteById(userId);
    }

    /**
     * 삭제, 갱신하는 userId 가 현재 요청을 보낸 유저의 user 인지를 확인.
     */
    private void validateUser(User user, Long userId) throws InvalidKeyException {
        Long currentUserId = user.getId();
        if (currentUserId != userId) {
            throw new InvalidKeyException("자신의 아이디만 바꿀 수 있습니다.");
        }
    }

}
