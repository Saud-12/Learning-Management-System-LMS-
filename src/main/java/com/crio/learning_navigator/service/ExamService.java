package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.ExamDto;

import java.util.List;

public interface ExamService {
    ExamDto createNewExam(Long subjectId);
    List<ExamDto> getAllExams();
    ExamDto getExamById(Long id);
    ExamDto updateExamById(Long id,ExamDto examDto);
    void deleteExamById(Long id);
}
