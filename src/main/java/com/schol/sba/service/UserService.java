package com.schol.sba.service;

import org.springframework.http.ResponseEntity;

import com.schol.sba.userdto.UserRequest;
import com.schol.sba.userdto.UserResponse;
import com.schol.sba.util.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest request);

	ResponseEntity<ResponseStructure<UserResponse>> getRegisteredUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	

}
