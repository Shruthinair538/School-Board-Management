package com.schol.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.schol.sba.requestdto.AcademicProgramRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.util.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicProgram(int schoolId,
			AcademicProgramRequest request);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAcademicProgram(int schoolId);

}
