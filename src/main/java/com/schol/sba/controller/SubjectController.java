package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.SubjectRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.responsedto.SubjectResponse;
import com.schol.sba.service.SubjectService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class SubjectController {
	
	@Autowired
	private SubjectService service;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/academicprograms/{programId}/subjects")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubjectList(@PathVariable int programId,@RequestBody SubjectRequest request){
		return service.addSubjectList(programId,request);
	}
	
	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects(){
		return service.findAllSubjects();
		
	}
	
	
	

}
