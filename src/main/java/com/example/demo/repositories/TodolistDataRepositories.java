package com.example.demo.repositories;
 
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.TodolistData;
 
@Repository
public interface TodolistDataRepositories 
    extends JpaRepository<TodolistData, Long> {
	List<TodolistData> findByStatus(TodolistData.Status status);
    List<TodolistData> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<TodolistData> findByStatusAndCreatedAtBetween(TodolistData.Status status, LocalDateTime startDate, LocalDateTime endDate);
 
}        
