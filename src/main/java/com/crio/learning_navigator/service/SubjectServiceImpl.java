package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
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
public class SubjectServiceImpl implements SubjectService{

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    @Override
    public SubjectDto createNewSubject(SubjectDto subjectDto) {
        log.info("Attempting to create a new subject");
        if(subjectRepository.existsBySubjectName(subjectDto.getSubjectName())){
            throw new IllegalArgumentException("Cannot create subject with name "+subjectDto.getSubjectName()+", already exists!");
        }
       Subject subject=modelMapper.map(subjectDto, Subject.class);
       return modelMapper.map(subjectRepository.save(subject),SubjectDto.class);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subject->modelMapper.map(subject,SubjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubjectDto getSubjectById(Long id) {
        Subject subject=subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject with id: "+id+" does not exists!"));
        return modelMapper.map(subject,SubjectDto.class);
    }

    @Override
    public SubjectDto updateSubjectById(Long id,SubjectDto subjectDto) {
        Subject subject=subjectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Subject with id: "+id+" does not exists!"));
        modelMapper.map(subjectDto,subject);
        return modelMapper.map(subjectRepository.save(subject),SubjectDto.class);
    }

    @Override
    public void deleteSubjectById(Long id) {
        if(!subjectRepository.existsById(id)){
            throw new ResourceNotFoundException("Subject with id: "+id+" does not exists!");
        }
        subjectRepository.deleteById(id);
    }
}
