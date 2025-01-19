package com.crio.learning_navigator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;

    @NotBlank(message = "name cannot be null or empty")
    @Size(min=4,max=15,message = "name should be within the range of 4-15 characters")
    private String name;
    private List<SubjectDto> enrolledSubjects;
    private List<ExamDto> enrolledExams;
}
