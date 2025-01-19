package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.entity.Exam;
import com.crio.learning_navigator.entity.Student;
import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.exception.ResourceNotFoundException;
import com.crio.learning_navigator.exception.StudentAlreadyEnrolledForExamException;
import com.crio.learning_navigator.exception.StudentAlreadyEnrolledInSubjectException;
import com.crio.learning_navigator.exception.StudentNotEnrolledForSubjectException;
import com.crio.learning_navigator.repository.ExamRepository;
import com.crio.learning_navigator.repository.StudentRepository;
import com.crio.learning_navigator.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final ExamRepository examRepository;
    private final ModelMapper modelMapper;

    @Override
    public StudentDto createNewStudent(StudentDto studentDto) {
      log.info("Attempting to create a new Student");
      Student student=modelMapper.map(studentDto, Student.class);
      if(student.getEnrolledSubjects()==null){
          student.setEnrolledSubjects(Set.of());
      }
      if(student.getEnrolledExams()==null){
          student.setEnrolledExams(Set.of());
      }
      return modelMapper.map(studentRepository.save(student), StudentDto.class);
    }

    @Override
    public List<StudentDto> getAllStudents() {
      return studentRepository.findAll().stream()
              .map(student->modelMapper.map(student,StudentDto.class))
              .collect(Collectors.toList());
    }

    @Override
    public StudentDto getStudentById(Long id) {
       Student student= studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student with id: "+id+" does not exists!"));
        return modelMapper.map(student,StudentDto.class);
    }

    @Override
    public StudentDto updateStudentById(Long id, StudentDto studentDto) {
        Student student=studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student with id: "+id+" does not exists!"));
        modelMapper.map(studentDto,student);
        return modelMapper.map(studentRepository.save(student),StudentDto.class);
    }

    @Transactional
    @Override
    public StudentDto enrollInSubject(Long studentId, Long subjectId) {
        Subject subject=subjectRepository.findById(subjectId).orElseThrow(()->new ResourceNotFoundException("Subject with id: "+subjectId+" does not exists!"));
        Student student=studentRepository.findById(studentId).orElseThrow(()->new ResourceNotFoundException("Student with id: "+studentId+" does not exists!"));

        if(student.getEnrolledSubjects().contains(subject)){
            throw new StudentAlreadyEnrolledInSubjectException("Student with id: "+studentId+" has already enrolled in subject: "+subject.getSubjectName());
        }
        student.getEnrolledSubjects().add(subject);
        studentRepository.save(student);

        return modelMapper.map(student,StudentDto.class);
    }

    @Transactional
    @Override
    public StudentDto enrollForExam(Long studentId, Long examId) {
        Student student=studentRepository.findById(studentId).orElseThrow(()->new ResourceNotFoundException("Student with id: "+studentId+" does not exists!"));
        Exam exam=examRepository.findById(examId).orElseThrow(()->new ResourceNotFoundException("Exam with id: "+examId+" does not exists!"));
        Subject subject=exam.getSubject();
        boolean isEnrolledInSubject=student.getEnrolledSubjects().contains(subject);
        boolean isEnrolledForExam=student.getEnrolledExams().contains(exam);

        if(!isEnrolledInSubject){
            throw new StudentNotEnrolledForSubjectException("Student must be enrolled for this subject "+subject.getSubjectName()+" to take up this exam");
        }

        if(isEnrolledForExam){
            throw new StudentAlreadyEnrolledForExamException("Student with id: "+studentId+" has already enrolled for this particular exam with id: "+examId);
        }

        student.getEnrolledExams().add(exam);
        exam.getStudentList().add(student);
        studentRepository.save(student);
        return modelMapper.map(student,StudentDto.class);
    }

    @Override
    public void deleteStudentById(Long id) {
        if(!studentRepository.existsById(id)){
            throw new ResourceNotFoundException("Student with id: "+id+" does not exists!");
        }
        studentRepository.deleteById(id);
    }

}
