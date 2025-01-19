package com.crio.learning_navigator;

import com.crio.learning_navigator.dto.ExamDto;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.entity.enums.SubjectName;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.ExamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ExamServiceImpl examService;

    private Subject subject;
    private Exam exam;
    private ExamDto examDto;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1L);
        subject.setSubjectName(SubjectName.MATHEMATICS);
        subject.setStudentList(new HashSet<>());

        exam = new Exam();
        exam.setId(1L);
        exam.setSubject(subject);
        exam.setExamName("MATHEMATICS EXAM");
        exam.setStudentList(new HashSet<>());

        examDto = new ExamDto();
        examDto.setId(1L);
        examDto.setExamName("MATHEMATICS EXAM");
    }

    @Test
    void createNewExam_Success() {
        // Arrange
        when(examRepository.existsBySubjectId(1L)).thenReturn(false);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(modelMapper.map(any(Exam.class), eq(ExamDto.class))).thenReturn(examDto);

        // Act
        ExamDto result = examService.createNewExam(1L);

        // Assert
        assertNotNull(result);
        assertEquals("MATHEMATICS EXAM", result.getExamName());
        verify(examRepository).existsBySubjectId(1L);
        verify(subjectRepository).findById(1L);
        verify(examRepository).save(any(Exam.class));
    }

    @Test
    void createNewExam_ThrowsException_WhenExamExists() {
        // Arrange
        when(examRepository.existsBySubjectId(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        examService.createNewExam(1L),
                "Exam for subject id: 1 already exists"
        );
        verify(examRepository).existsBySubjectId(1L);
        verify(subjectRepository, never()).findById(any());
        verify(examRepository, never()).save(any());
    }

    @Test
    void createNewExam_ThrowsException_WhenSubjectNotFound() {
        // Arrange
        when(examRepository.existsBySubjectId(1L)).thenReturn(false);
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                        examService.createNewExam(1L),
                "Cannot create an exam entity for subject with id: 1, subject not found"
        );
        verify(examRepository).existsBySubjectId(1L);
        verify(subjectRepository).findById(1L);
        verify(examRepository, never()).save(any());
    }

    @Test
    void getAllExams_Success() {
        // Arrange
        List<Exam> exams = List.of(exam);
        when(examRepository.findAll()).thenReturn(exams);
        when(modelMapper.map(any(Exam.class), eq(ExamDto.class))).thenReturn(examDto);

        // Act
        List<ExamDto> result = examService.getAllExams();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("MATHEMATICS EXAM", result.get(0).getExamName());
        verify(examRepository).findAll();
    }

    @Test
    void getAllExams_ReturnsEmptyList() {
        // Arrange
        when(examRepository.findAll()).thenReturn(List.of());

        // Act
        List<ExamDto> result = examService.getAllExams();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(examRepository).findAll();
    }

    @Test
    void getExamById_Success() {
        // Arrange
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(modelMapper.map(any(Exam.class), eq(ExamDto.class))).thenReturn(examDto);

        // Act
        ExamDto result = examService.getExamById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("MATHEMATICS EXAM", result.getExamName());
        verify(examRepository).findById(1L);
    }

    @Test
    void getExamById_ThrowsResourceNotFoundException() {
        // Arrange
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                        examService.getExamById(1L),
                "Exam with id: 1 does not exists!"
        );
        verify(examRepository).findById(1L);
    }

    @Test
    void createNewExam_WithDifferentSubjects() {
        // Arrange
        Subject physicsSubject = new Subject();
        physicsSubject.setId(2L);
        physicsSubject.setSubjectName(SubjectName.PHYSICS);

        Exam physicsExam = new Exam();
        physicsExam.setId(2L);
        physicsExam.setSubject(physicsSubject);
        physicsExam.setExamName("PHYSICS EXAM");

        ExamDto physicsExamDto = new ExamDto();
        physicsExamDto.setId(2L);
        physicsExamDto.setExamName("PHYSICS EXAM");

        when(examRepository.existsBySubjectId(2L)).thenReturn(false);
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(physicsSubject));
        when(examRepository.save(any(Exam.class))).thenReturn(physicsExam);
        when(modelMapper.map(any(Exam.class), eq(ExamDto.class))).thenReturn(physicsExamDto);

        // Act
        ExamDto result = examService.createNewExam(2L);

        // Assert
        assertNotNull(result);
        assertEquals("PHYSICS EXAM", result.getExamName());
        verify(examRepository).existsBySubjectId(2L);
        verify(subjectRepository).findById(2L);
        verify(examRepository).save(any(Exam.class));
    }
}
