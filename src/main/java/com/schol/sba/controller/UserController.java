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

import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;
import com.schol.sba.requestdto.UserRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.service.UserService;
import com.schol.sba.util.ResponseStructure;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(@RequestBody UserRequest request){
		return userService.registerAdmin(request);
		
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("users")
	public ResponseEntity<ResponseStructure<UserResponse>> register(@RequestBody  UserRequest request){
		return userService.registerUser(request);
		
	}
	
	@GetMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisteredUser(@PathVariable int userId){
		return userService.getRegisteredUser(userId);
	 
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId){
		return userService.deleteUser(userId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("academicprograms/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> assignUser(@PathVariable int userId,@PathVariable int programId){
		return userService.assignUser(userId,programId);
	 
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("subjects/{subjectId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(@PathVariable int subjectId,@PathVariable int userId){
		return userService.addSubjectToTeacher(subjectId,userId);
	 
	}
	
	
	
	
	

}
