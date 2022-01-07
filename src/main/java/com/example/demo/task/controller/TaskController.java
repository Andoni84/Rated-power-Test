package com.example.demo.task.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.task.dto.TaskDTO;
import com.example.demo.task.exception.TaskNotFoundException;
import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;
import com.example.demo.task.service.TaskService;

@RestController
@RequestMapping("/api")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping("/tasks")
	public List<TaskEntity> findAllTasks(@RequestParam(value = "completed", required = false) Boolean completed,
			@RequestParam(value = "priority", required = false) TaskPriority priority, Sort sort) {
		return taskService.findAll(completed, priority, sort);
	}

	@GetMapping("/task/{id}")
	public TaskEntity findTask(@PathVariable int id) {
		return taskService.findById(id).orElseThrow(TaskNotFoundException::new);
	}

	@PostMapping("/task")
	@ResponseStatus(code = HttpStatus.CREATED)
	public TaskEntity createTask(@Valid @RequestBody TaskDTO taskDTO) {
		TaskEntity taskEntity = taskDTO.getTaskEntity();
		taskEntity.setId(null);
		if (taskEntity.getListSubTasks() != null) {
			taskEntity.getListSubTasks().forEach(st -> st.setId(null));
		}
		return taskService.save(taskEntity);
	}

	@PutMapping("/task/{id}")
	public TaskEntity updateTask(@PathVariable int id, @Valid @RequestBody TaskDTO taskDTO) {
		Optional<TaskEntity> taskEntityOptional = taskService.findById(id);
		if (!taskEntityOptional.isPresent()) {
			throw new TaskNotFoundException();
		}

		TaskEntity taskEntity = taskDTO.getTaskEntity();
		taskEntity.setId(id);
		return taskService.update(taskEntity);
	}

	@DeleteMapping("/task/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteTask(@PathVariable int id) {
		Optional<TaskEntity> taskEntityOptional = taskService.findById(id);
		if (!taskEntityOptional.isPresent()) {
			throw new TaskNotFoundException();
		}

		taskService.deleteById(id);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> validationError(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

	}
}
