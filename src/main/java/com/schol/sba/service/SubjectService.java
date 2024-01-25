package com.schol.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.schol.sba.requestdto.SubjectRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.responsedto.SubjectResponse;
import com.schol.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubjectList(int programId, SubjectRequest request);

	ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects();

	
}
