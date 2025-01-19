package com.crio.learning_navigator;

import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.entity.enums.SubjectName;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.SubjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private Subject subject;
    private SubjectDto subjectDto;

    @BeforeEach
    void setUp() {
        // Initialize Subject
        subject = new Subject();
        subject.setId(1L);
        subject.setSubjectName(SubjectName.MATHEMATICS);
        subject.setStudentList(new HashSet<>());

        // Initialize SubjectDto
        subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setSubjectName(SubjectName.MATHEMATICS);
    }

    @Test
    void createNewSubject_Success() {
        // Arrange
        when(subjectRepository.existsBySubjectName(any(SubjectName.class))).thenReturn(false);
        when(modelMapper.map(any(SubjectDto.class), eq(Subject.class))).thenReturn(subject);
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
        when(modelMapper.map(any(Subject.class), eq(SubjectDto.class))).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.createNewSubject(subjectDto);

        // Assert
        assertNotNull(result);
        assertEquals(SubjectName.MATHEMATICS, result.getSubjectName());
        verify(subjectRepository).save(any(Subject.class));
        verify(subjectRepository).existsBySubjectName(any(SubjectName.class));
    }

    @Test
    void createNewSubject_ThrowsException_WhenSubjectExists() {
        // Arrange
        when(subjectRepository.existsBySubjectName(any(SubjectName.class))).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        subjectService.createNewSubject(subjectDto),
                "Cannot create subject with name MATHEMATICS, already exists!"
        );
        verify(subjectRepository).existsBySubjectName(any(SubjectName.class));
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    void getAllSubjects_Success() {
        // Arrange
        List<Subject> subjects = List.of(subject);
        when(subjectRepository.findAll()).thenReturn(subjects);
        when(modelMapper.map(any(Subject.class), eq(SubjectDto.class))).thenReturn(subjectDto);

        // Act
        List<SubjectDto> result = subjectService.getAllSubjects();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(SubjectName.MATHEMATICS, result.get(0).getSubjectName());
        verify(subjectRepository).findAll();
    }

    @Test
    void getAllSubjects_ReturnsEmptyList() {
        // Arrange
        when(subjectRepository.findAll()).thenReturn(List.of());

        // Act
        List<SubjectDto> result = subjectService.getAllSubjects();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository).findAll();
    }

    @Test
    void getSubjectById_Success() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(modelMapper.map(any(Subject.class), eq(SubjectDto.class))).thenReturn(subjectDto);

        // Act
        SubjectDto result = subjectService.getSubjectById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(SubjectName.MATHEMATICS, result.getSubjectName());
        verify(subjectRepository).findById(1L);
    }

    @Test
    void getSubjectById_ThrowsResourceNotFoundException() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                        subjectService.getSubjectById(1L),
                "Subject with id: 1 does not exists!"
        );
        verify(subjectRepository).findById(1L);
    }

}
