package com.crio.learning_navigator.controller;

import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping ()
    public ResponseEntity<SubjectDto> createNewSubject(@RequestBody SubjectDto subjectDto){
        return new ResponseEntity<>(subjectService.createNewSubject(subjectDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping()
    public ResponseEntity<List<SubjectDto>> getAllSubject(){
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDto> updateSubjectById(@PathVariable Long id,@RequestBody SubjectDto subjectDto){
        return ResponseEntity.ok(subjectService.updateSubjectById(id,subjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubjectById(@PathVariable Long id){
        subjectService.deleteSubjectById(id);
        return ResponseEntity.noContent().build();
    }
}
