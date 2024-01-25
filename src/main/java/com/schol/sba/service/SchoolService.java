package com.schol.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.schol.sba.entity.School;
import com.schol.sba.requestdto.SchoolRequest;
import com.schol.sba.responsedto.SchoolResponse;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.util.ResponseStructure;

public interface SchoolService {

	ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest request);

	ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest request);

	ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId);

	ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchools();

	ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId);
	
	
}
