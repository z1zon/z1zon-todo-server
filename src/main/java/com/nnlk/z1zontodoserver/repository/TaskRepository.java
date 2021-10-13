package com.nnlk.z1zontodoserver.repository;

import com.nnlk.z1zontodoserver.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByIdAndUserId(Long taskId, Long userId);
    @Query("select t from Task t join fetch t.category ")
    List<Task> findAllByUserId(Long userId);
    List<Task> findAllByCategoryId(Long categoryId);

}
