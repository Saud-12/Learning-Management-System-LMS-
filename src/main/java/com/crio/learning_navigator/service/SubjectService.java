package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.SubjectDto;

import java.util.List;

public interface SubjectService {
    SubjectDto createNewSubject(SubjectDto subjectDto);
    List<SubjectDto> getAllSubjects();
    SubjectDto getSubjectById(Long id);
    SubjectDto updateSubjectById(Long id,SubjectDto subjectDto);
    void deleteSubjectById(Long id);
}
