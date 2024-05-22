package com.example.demo;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repositories.TodolistDataRepositories;
 
@RestController
@RequestMapping("/todolist")
@CrossOrigin
public class TodolistController {
 
  @Autowired
  TodolistDataRepositories repository;
   
  @GetMapping
  public ResponseEntity<List<TodolistData>> getAllTodolist(
      @RequestParam(required = false) TodolistData.Status status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

      List<TodolistData> todolist;
      if (status != null && startDate != null && endDate != null) {
          todolist = repository.findByStatusAndCreatedAtBetween(status, startDate, endDate);
      } else if (status != null) {
          todolist = repository.findByStatus(status);
      } else if (startDate != null && endDate != null) {
          todolist = repository.findByCreatedAtBetween(startDate, endDate);
      } else {
          todolist = repository.findAll();
      }
      return ResponseEntity.ok().body(todolist);
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<TodolistData> updateTodolist(
      @PathVariable Long id,
      @RequestBody TodolistData updatedTodolistData) {

      Optional<TodolistData> optionalTodolistData = repository.findById(id);
      if (optionalTodolistData.isPresent()) {
          TodolistData existingTodolistData = optionalTodolistData.get();
          existingTodolistData.setTitle(updatedTodolistData.getTitle());
          existingTodolistData.setDescription(updatedTodolistData.getDescription());
          existingTodolistData.setStatus(updatedTodolistData.getStatus());

          TodolistData savedTodolistData = repository.save(existingTodolistData);
          return ResponseEntity.ok(savedTodolistData);
      } else {
          return ResponseEntity.notFound().build();
      }
  }
  
  @PostMapping
  public ResponseEntity<TodolistData> addTodolist(@RequestBody TodolistData newTodolistData) {
      TodolistData savedTodolistData = repository.save(newTodolistData);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedTodolistData);
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTodolist(@PathVariable Long id) {
      if (repository.existsById(id)) 
      {
          repository.deleteById(id);
          return ResponseEntity.noContent().build();
      } else {
          return ResponseEntity.notFound().build();
      }
  }
}    
