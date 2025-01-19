package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.StudentDto;

import java.util.List;

public interface StudentService {
    StudentDto createNewStudent(StudentDto studentDto);
    List<StudentDto> getAllStudents();
    StudentDto getStudentById(Long id);
    StudentDto updateStudentById(Long id,StudentDto studentDto);
    StudentDto enrollInSubject(Long studentId,Long subjectId);
    StudentDto enrollForExam(Long studentId,Long examId);
    void deleteStudentById(Long id);
}
