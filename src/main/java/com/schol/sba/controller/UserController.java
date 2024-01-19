package com.schol.sba.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.schol.sba.requestdto.UserRequest;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.service.UserService;
import com.schol.sba.util.ResponseStructure;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> register(@RequestBody  UserRequest request){
		return userService.registerUser(request);
		
	}
	
	@GetMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisteredUser(@PathVariable int userId){
		return userService.getRegisteredUser(userId);
	 
	}
	
	@DeleteMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(@PathVariable int userId){
		return userService.deleteUser(userId);
	}
	
	@PutMapping("academicprograms/{programId}/users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponse>> assignUser(@PathVariable int userId,@PathVariable int programId){
		return userService.assignUser(userId,programId);
	 
	}
	
	

}
