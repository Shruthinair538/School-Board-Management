package com.schol.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.service.ClassService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class ClassHourController {
	
	@Autowired
	private ClassService service;
	
	@PostMapping("academicprograms/{programId}/classhours")
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(@PathVariable int programId,@RequestBody ClassHourRequest request){
		return service.generateClassHour(programId,request);
	}

}
