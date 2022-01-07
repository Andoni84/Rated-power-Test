package com.example.demo.task.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import com.example.demo.task.model.SubTaskEntity;
import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;

@Component
public class TaskDTO {
	private Integer id;

	@NotBlank(message = "Description cannot be empty")
	@Length(max = 500, message = "Description field must not exceeded 500 characters")
	private String description;

	private boolean completed;

	@NotNull(message = "Priority cannot be empty")
	private TaskPriority priority;

	@Valid
	private List<SubTaskDTO> listSubTasks;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public List<SubTaskDTO> getListSubTasks() {
		return listSubTasks;
	}

	public void setListSubTasks(List<SubTaskDTO> listSubTasks) {
		this.listSubTasks = listSubTasks;
	}

	public TaskEntity getTaskEntity() {
		TaskEntity taskEntity = new TaskEntity();
		taskEntity.setId(id);
		taskEntity.setDescription(description);
		taskEntity.setCompleted(completed);
		taskEntity.setPriority(priority);

		List<SubTaskEntity> listSubTaskEntities = new ArrayList<>();
		if (listSubTasks != null) {
			for (SubTaskDTO subTaskDTO : listSubTasks) {
				listSubTaskEntities.add(subTaskDTO.getSubTaskEntity(taskEntity));
			}
		}

		taskEntity.setListSubTasks(listSubTaskEntities);

		return taskEntity;
	}

}
