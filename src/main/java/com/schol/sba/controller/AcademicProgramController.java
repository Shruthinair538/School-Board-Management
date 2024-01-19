package com.schol.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.AcademicProgramRequest;
import com.schol.sba.requestdto.AcademicProgramResponse;
import com.schol.sba.service.AcademicProgramService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class AcademicProgramController {
	
	@Autowired
	private AcademicProgramService academicService;
	
	@PostMapping("schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicProgram(@PathVariable int schoolId,@RequestBody AcademicProgramRequest request){
		return academicService.addAcademicProgram(schoolId,request);
	}

}
