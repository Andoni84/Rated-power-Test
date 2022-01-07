package com.example.demo.task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;

public interface TaskService {

	public List<TaskEntity> findAll(Boolean completed, TaskPriority priority, Sort sort);

	public Optional<TaskEntity> findById(Integer id);

	public TaskEntity save(TaskEntity taskEntity);

	public TaskEntity update(TaskEntity taskEntity);

	public void deleteById(Integer id);

}
