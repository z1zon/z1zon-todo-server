package com.nnlk.z1zontodoserver.repository;

import com.nnlk.z1zontodoserver.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
