package com.nnlk.z1zontodoserver.repository;

import com.nnlk.z1zontodoserver.domain.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    List<SubTask> findAllByTaskId(Long taskId);
}
