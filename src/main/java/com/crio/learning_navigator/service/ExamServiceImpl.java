package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.ExamDto;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{

    private final ExamRepository examRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    public ExamDto createNewExam(Long subjectId){
        log.info("Attempting to create a new Exam");
        if(examRepository.existsBySubjectId(subjectId)){
            throw new IllegalArgumentException("Exam for subject id: "+subjectId+" already exists");
        }

        Subject subject=subjectRepository.findById(subjectId).orElseThrow(()->new ResourceNotFoundException("Cannot create an exam entity for subject with id: "+subjectId+", subject not found"));
        Exam exam=Exam.builder()
                .subject(subject)
                .examName(subject.getSubjectName()+" EXAM")
                .build();
        return modelMapper.map(examRepository.save(exam),ExamDto.class);
    }

    public List<ExamDto> getAllExams(){
        return examRepository.findAll().stream()
                .map(exam -> modelMapper.map(exam,ExamDto.class))
                .collect(Collectors.toList());
    }
    public ExamDto getExamById(Long id){
        Exam exam=examRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Exam with id: "+id+" does not exists!"));
        return modelMapper.map(exam,ExamDto.class);
    }

    public ExamDto updateExamById(Long id,ExamDto examDto){
        Exam exam=examRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Exam with id: "+id+" does not exists!"));
        modelMapper.map(examDto,exam);
        return modelMapper.map(examRepository.save(exam),ExamDto.class);
    }

    @Override
    public void deleteExamById(Long id) {
        if(!examRepository.existsById(id)){
            throw new ResourceNotFoundException("Exam with id: "+id+" does not exists!");
        }
        examRepository.deleteById(id);
    }
}
