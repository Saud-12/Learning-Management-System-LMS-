package com.crio.learning_navigator.dto;

import com.crio.learning_navigator.entity.enums.SubjectName;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    private Long id;

    @NotBlank(message = "subject name cannot be null or empty")
    private SubjectName subjectName;

}
