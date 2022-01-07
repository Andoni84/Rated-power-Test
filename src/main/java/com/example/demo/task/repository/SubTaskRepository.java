package com.example.demo.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.task.model.SubTaskEntity;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTaskEntity, Integer> {

}
