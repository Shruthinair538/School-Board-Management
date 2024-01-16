package com.schol.sba.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;
import com.schol.sba.exception.AdminAlreadyExistException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.service.UserService;
import com.schol.sba.userdto.UserRequest;
import com.schol.sba.userdto.UserResponse;
import com.schol.sba.util.ResponseStructure;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> userStructure;
	
	private User mapToUser(UserRequest request) {
		return User.builder()
				.userName(request.getUserName())
		        .userPassword(request.getUserPassword())
		        .userFirstName(request.getUserFirstName())
		        .userLastName(request.getUserLastName())
		        .userContactNo(request.getUserContactNo())
		        .userEmail(request.getUserEmail())
		        .userRole(request.getUserRole())
		        .build();
	}
	
	private UserResponse mapToResponse(User response) {
		return UserResponse.builder()
				.userId(response.getUserId())
				.userName(response.getUserName())
				.userFirstName(response.getUserFirstName())
				.userLastName(response.getUserLastName())
				.userContactNo(response.getUserContactNo())
				.userEmail(response.getUserEmail())
				.userRole(response.getUserRole())
				.build();
			   
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest request) {
		if(request.getUserRole()==UserRole.ADMIN && userRepo.existsByUserRole(UserRole.ADMIN))
		 {
			throw new AdminAlreadyExistException("Can have only one Admin!!!");
			
		 }else {
			User user = userRepo.save(mapToUser(request));
			userStructure.setStatusCode(HttpStatus.CREATED.value());
			userStructure.setMessage("User object registered successfully!!!");
			userStructure.setData(mapToResponse(user));
	
	
		return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.CREATED);
		 }
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> getRegisteredUser(int userId) {
		User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundByIdException("User with this ID doesn't exist!!1"));
		
		userStructure.setStatusCode(HttpStatus.FOUND.value());
		userStructure.setMessage("Data found successfully!!!");
		userStructure.setData(mapToResponse(user));
		
		return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.FOUND);
	}

	

	
}
