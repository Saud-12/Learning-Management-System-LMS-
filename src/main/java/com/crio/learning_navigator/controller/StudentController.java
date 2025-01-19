package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping()
    public ResponseEntity<StudentDto> createNewStudent(@RequestBody StudentDto studentDto){
        return new ResponseEntity<>(studentService.createNewStudent(studentDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id){
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping()
    public ResponseEntity<List<StudentDto>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudentById(@PathVariable Long id,@RequestBody StudentDto studentDto){
        return ResponseEntity.ok(studentService.updateStudentById(id,studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{studentId}/subjects/{subjectId}")
    public ResponseEntity<StudentDto> enrollInSubject(@PathVariable Long studentId,@PathVariable Long subjectId){
        return ResponseEntity.ok(studentService.enrollInSubject(studentId,subjectId));
    }

    @PostMapping("/{studentId}/exams/{examId}")
    public ResponseEntity<StudentDto> enrollForExam(@PathVariable Long studentId,@PathVariable Long examId){
        return ResponseEntity.ok(studentService.enrollForExam(studentId,examId));
    }

}
