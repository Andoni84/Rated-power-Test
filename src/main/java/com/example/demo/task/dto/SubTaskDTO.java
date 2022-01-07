package com.example.demo.task.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import com.example.demo.task.model.SubTaskEntity;
import com.example.demo.task.model.TaskEntity;

@Component
public class SubTaskDTO {
	private Integer id;

	@NotBlank(message = "Subtask description cannot be empty")
	@Length(max = 500, message = "Subtask description field must not exceeded 500 characters")
	private String description;

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

	public SubTaskEntity getSubTaskEntity(TaskEntity taskEntity) {
		SubTaskEntity subtaskEntity = new SubTaskEntity();
		subtaskEntity.setId(id);
		subtaskEntity.setDescription(description);
		subtaskEntity.setTask(taskEntity);
		return subtaskEntity;
	}

}
