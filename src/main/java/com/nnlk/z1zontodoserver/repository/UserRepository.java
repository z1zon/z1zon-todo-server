package com.nnlk.z1zontodoserver.repository;

import com.nnlk.z1zontodoserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

}
