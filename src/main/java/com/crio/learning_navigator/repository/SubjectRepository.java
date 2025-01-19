package com.crio.learning_navigator.repository;

import com.crio.learning_navigator.entity.Subject;
import com.crio.learning_navigator.entity.enums.SubjectName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    Boolean existsBySubjectName(SubjectName subjectName);
}
