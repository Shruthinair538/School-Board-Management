package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.ClassHourDTO;
import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.service.ClassService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class ClassHourController {
	
	@Autowired
	private ClassService service;
	
	
	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(@PathVariable int programId){
		return service.generateClassHour(programId);
	}
	
	@PutMapping("/class-hours")
	public ResponseEntity<String> updateClassHour(@RequestBody List<ClassHourDTO> request){
		service.updateClassHour(request);
		return ResponseEntity.ok("Class hours updated successfully!!");
	}
	

}
