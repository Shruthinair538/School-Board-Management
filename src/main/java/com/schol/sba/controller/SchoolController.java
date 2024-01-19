package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(@PathVariable int userId,  @RequestBody SchoolRequest request ){
		return schoolService.createSchool(userId,request);
		
	}
	
	
	
	
	

}
