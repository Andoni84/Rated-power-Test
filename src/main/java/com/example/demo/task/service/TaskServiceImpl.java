package com.example.demo.task.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.task.exception.TaskNotFoundException;
import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;
import com.example.demo.task.repository.SubTaskRepository;
import com.example.demo.task.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	SubTaskRepository subTaskRepository;

	@Override
	public List<TaskEntity> findAll(Boolean completed, TaskPriority priority, Sort sort) {
		List<Boolean> completedList = new ArrayList<>();
		if (completed == null) {
			completedList.add(Boolean.TRUE);
			completedList.add(Boolean.FALSE);
		} else {
			completedList.add(completed);
		}

		List<TaskPriority> priorityList = new ArrayList<>();
		if (priority == null) {
			priorityList.addAll(Arrays.asList(TaskPriority.values()));
		} else {
			priorityList.add(priority);
		}

		return taskRepository.findByCompletedInAndPriorityIn(completedList, priorityList, sort);
	}

	@Override
	public Optional<TaskEntity> findById(Integer id) {
		return taskRepository.findById(id);
	}

	@Override
	public TaskEntity save(TaskEntity taskEntity) {
		return taskRepository.save(taskEntity);
	}

	@Override
	@Transactional
	public TaskEntity update(TaskEntity taskEntity) {
		TaskEntity taskEntityDB = findById(taskEntity.getId()).orElseThrow(TaskNotFoundException::new);
		if (taskEntityDB.getListSubTasks() != null) {
			taskEntityDB.getListSubTasks().forEach(st -> {
				if (taskEntity.getListSubTasks() != null && !taskEntity.getListSubTasks().contains(st)) {
					st.setTask(null);
					subTaskRepository.save(st);
				}
			});
		}
		if (taskEntity.getListSubTasks() != null) {
			taskEntity.getListSubTasks().forEach(st -> subTaskRepository.save(st));
		}

		return taskRepository.save(taskEntity);
	}

	@Override
	public void deleteById(Integer id) {
		taskRepository.deleteById(id);
	}

}
