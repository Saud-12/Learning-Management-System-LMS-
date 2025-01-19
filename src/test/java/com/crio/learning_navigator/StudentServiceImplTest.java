package com.crio.learning_navigator;

import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Student;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.entity.enums.SubjectName;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.exception.StudentAlreadyEnrolledForExamException;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.StudentRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import com.crio.learning_navigator.service.StudentServiceImpl;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentDto studentDto;
    private Subject subject;
    private Exam exam;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEnrolledSubjects(new HashSet<>());
        student.setEnrolledExams(new HashSet<>());

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setName("John Doe");

        subject = new Subject();
        subject.setId(1L);
        subject.setSubjectName(SubjectName.MATHEMATICS); // Using enum value
        subject.setStudentList(new HashSet<>());

        exam = new Exam();
        exam.setId(1L);
        exam.setExamName("Mathematics Final Exam"); // Updated to match subject
        exam.setSubject(subject);
        exam.setStudentList(new HashSet<>());
    }

    @Test
    void createNewStudent_Success() {
        // Arrange
        when(modelMapper.map(any(StudentDto.class), eq(Student.class))).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(studentDto);

        // Act
        StudentDto result = studentService.createNewStudent(studentDto);

        // Assert
        assertNotNull(result);
        assertEquals(studentDto.getName(), result.getName());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void getAllStudents_Success() {
        // Arrange
        List<Student> students = List.of(student);
        when(studentRepository.findAll()).thenReturn(students);
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(studentDto);

        // Act
        List<StudentDto> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(studentDto);

        // Act
        StudentDto result = studentService.getStudentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(studentDto.getId(), result.getId());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_ThrowsResourceNotFoundException() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                studentService.getStudentById(1L)
        );
        verify(studentRepository).findById(1L);
    }

    @Test
    void enrollInSubject_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(studentDto);

        // Act
        StudentDto result = studentService.enrollInSubject(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(studentRepository).findById(1L);
        verify(subjectRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void enrollInSubject_AlreadyEnrolled() {
        // Arrange
        student.setEnrolledSubjects(Set.of(subject));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        studentService.enrollInSubject(1L, 1L),
                "Student with id: 1 has already enrolled in subject: MATHEMATICS"
        );
    }

    @Test
    void enrollForExam_Success() {
        // Arrange
        student.setEnrolledSubjects(Set.of(subject));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(modelMapper.map(any(Student.class), eq(StudentDto.class))).thenReturn(studentDto);

        // Act
        StudentDto result = studentService.enrollForExam(1L, 1L);

        // Assert
        assertNotNull(result);
        verify(studentRepository).findById(1L);
        verify(examRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void enrollForExam_NotEnrolledInSubject() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        studentService.enrollForExam(1L, 1L),
                "Student must be enrolled for this subject MATHEMATICS to take up this exam"
        );
    }

    @Test
    void enrollForExam_AlreadyEnrolledInExam() {
        // Arrange
        student.setEnrolledSubjects(Set.of(subject));
        student.setEnrolledExams(Set.of(exam));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        // Act & Assert
        assertThrows(StudentAlreadyEnrolledForExamException.class, () ->
                        studentService.enrollForExam(1L, 1L),
                "Student with id: 1 has already enrolled for this particular exam with id: 1"
        );
    }

    @Test
    void enrollForExam_WithDifferentSubjects() {
        // Arrange
        Subject physicsSubject = new Subject();
        physicsSubject.setId(2L);
        physicsSubject.setSubjectName(SubjectName.PHYSICS);

        student.setEnrolledSubjects(Set.of(physicsSubject));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam)); // exam is for MATHEMATICS

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                        studentService.enrollForExam(1L, 1L),
                "Student must be enrolled for this subject MATHEMATICS to take up this exam"
        );
    }
}