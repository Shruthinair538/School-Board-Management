package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schol.sba.entity.School;
import com.schol.sba.requestdto.SchoolRequest;
import com.schol.sba.responsedto.SchoolResponse;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.service.SchoolService;
import com.schol.sba.util.ResponseStructure;

@Controller
public class SchoolController {
	
	@Autowired
	private SchoolService schoolService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("users/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool( @RequestBody SchoolRequest request ){
		return schoolService.createSchool(request);
		
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(@PathVariable int schoolId,@RequestBody SchoolRequest request){
		return schoolService.updateSchool(schoolId,request);
	}
	
	@GetMapping("schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(@PathVariable int schoolId){
		return schoolService.findSchoolById(schoolId);
	}
	
	@GetMapping("schools")
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchools(){
		return schoolService.findAllSchools();
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(@PathVariable int schoolId){
		return schoolService.deleteSchool(schoolId);
	}
	
	
	
	
	
	
	
	
	
	
	

}
