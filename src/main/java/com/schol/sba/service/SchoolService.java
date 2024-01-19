package com.schol.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.schol.sba.entity.School;
import com.schol.sba.requestdto.SchoolRequest;
import com.schol.sba.requestdto.SchoolResponse;
import com.schol.sba.requestdto.UserResponse;
import com.schol.sba.util.ResponseStructure;

public interface SchoolService {

	ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(int userId, SchoolRequest request);
	
	
}
