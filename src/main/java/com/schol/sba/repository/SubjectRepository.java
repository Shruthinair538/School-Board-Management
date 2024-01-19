package com.schol.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schol.sba.entity.Subject;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer>{
	
	Optional<Subject> findBySubjectName(String subjectName);

}
