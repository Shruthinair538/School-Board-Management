package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.AcademicProgramRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.service.AcademicProgramService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class AcademicProgramController {
	
	@Autowired
	private AcademicProgramService academicService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicProgram(@PathVariable int schoolId,@RequestBody AcademicProgramRequest request){
		return academicService.addAcademicProgram(schoolId,request);
	}
	
	
	@GetMapping("schools/{schoolId}/academicprograms")
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAcademicProgram(@PathVariable int schoolId){
		return academicService.findAcademicProgram(schoolId);
	}
	
	
	
	

}
