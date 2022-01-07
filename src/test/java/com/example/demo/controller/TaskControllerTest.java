package com.example.demo.controller;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.task.model.TaskEntity;
import com.example.demo.task.model.TaskPriority;

import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

	@LocalServerPort
	private int port;

	private TaskEntity taskTest;

	@BeforeEach
	public void setup() {
		TaskEntity task1 = new TaskEntity();
		task1.setDescription("task one");
		task1.setPriority(TaskPriority.LOW);

		given().baseUri("http://localhost:" + port).contentType(ContentType.JSON).body(task1).when().post("/api/task")
				.then().assertThat().statusCode(HTTP_CREATED).extract().as(TaskEntity.class);

		TaskEntity task2 = new TaskEntity();
		task2.setCompleted(true);
		task2.setDescription("task two");
		task2.setPriority(TaskPriority.LOW);

		given().baseUri("http://localhost:" + port).contentType(ContentType.JSON).body(task2).when().post("/api/task")
				.then().assertThat().statusCode(HTTP_CREATED).extract().as(TaskEntity.class);

		TaskEntity task3 = new TaskEntity();
		task3.setDescription("task three");
		task3.setPriority(TaskPriority.HIGH);

		taskTest = given().baseUri("http://localhost:" + port).contentType(ContentType.JSON).body(task3).when()
				.post("/api/task").then().assertThat().statusCode(HTTP_CREATED).extract().as(TaskEntity.class);

	}

	@Test
	void tasksList() {
		given().baseUri("http://localhost:" + port).when().get("/api/tasks").then().assertThat().statusCode(HTTP_OK)
				.extract().as(TaskEntity[].class);

	}

	@Test
	void tasksFilteredSorted() {
		TaskEntity[] tasks = given().baseUri("http://localhost:" + port).when()
				.get("/api/tasks?priority=LOW&sort=completed,ASC").then().assertThat().statusCode(HTTP_OK).extract()
				.as(TaskEntity[].class);

		assertThat(tasks[0].isCompleted(), is(false));
	}

	@Test
	void tasksUpdate() {
		taskTest.setCompleted(true);
		TaskEntity task = given().baseUri("http://localhost:" + port).contentType(ContentType.JSON).body(taskTest)
				.when().put("/api/task/" + taskTest.getId()).then().assertThat().statusCode(HTTP_OK).extract()
				.as(TaskEntity.class);

		assertThat(task.isCompleted(), is(true));
	}

	@Test
	void tasksDelete() {
		given().baseUri("http://localhost:" + port).when().delete("/api/task/" + taskTest.getId()).then().assertThat()
				.statusCode(HTTP_OK);

	}

	@Test
	void tasksNotFound() {
		given().baseUri("http://localhost:" + port).when().get("/api/task/-11").then().assertThat()
				.statusCode(HTTP_NOT_FOUND);
	}

	@Test
	void tasksBadRequest() {
		taskTest.setDescription("");
		given().baseUri("http://localhost:" + port).contentType(ContentType.JSON).body(taskTest).when()
				.post("/api/task/").then().assertThat().statusCode(HTTP_BAD_REQUEST);

	}
}
