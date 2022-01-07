package com.example.demo.task.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
	public List<TaskEntity> findByCompletedInAndPriorityIn(List<Boolean> completed, List<TaskPriority> priority,
			Sort sort);

}
