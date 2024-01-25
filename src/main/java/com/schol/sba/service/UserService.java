package com.schol.sba.service;

import org.springframework.http.ResponseEntity;

import com.schol.sba.enums.UserRole;
import com.schol.sba.requestdto.UserRequest;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.util.ResponseStructure;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest request);

	ResponseEntity<ResponseStructure<UserResponse>> getRegisteredUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId);

	ResponseEntity<ResponseStructure<UserResponse>> assignUser(int userId, int programId);

	ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(int subjectId, int userId);

	ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest request);

	

}
