package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.ExamDto;
import com.crio.learning_navigator.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("/subjects/{subjectId}")
    public ResponseEntity<ExamDto> createNewExam(@PathVariable Long subjectId){
        return new ResponseEntity<>(examService.createNewExam(subjectId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDto> getExamById(@PathVariable Long id){
        return ResponseEntity.ok(examService.getExamById(id));
    }

    @GetMapping()
    public ResponseEntity<List<ExamDto>> getAllExams(){
        return ResponseEntity.ok(examService.getAllExams());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDto> updateExamById(@PathVariable Long id,@RequestBody ExamDto examDto){
        return ResponseEntity.ok(examService.updateExamById(id,examDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamById(@PathVariable Long id){
        examService.deleteExamById(id);
        return ResponseEntity.noContent().build();
    }
}
