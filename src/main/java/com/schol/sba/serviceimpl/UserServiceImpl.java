package com.schol.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.School;
import com.schol.sba.entity.Subject;
import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;
import com.schol.sba.exception.AcademicProgramNorFoundException;
import com.schol.sba.exception.AdminAlreadyExistException;
import com.schol.sba.exception.AdminCannotBeAssignedToProgramException;
import com.schol.sba.exception.AdminCannotBeRegisteredException;
import com.schol.sba.exception.OnlyTeacherCanBeAssignedException;
import com.schol.sba.exception.SchoolNotFoundException;
import com.schol.sba.exception.SubjectNotFoundException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.SubjectRepository;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.requestdto.UserRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.responsedto.UserResponse;
import com.schol.sba.service.UserService;
import com.schol.sba.util.ResponseStructure;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> userStructure;
	
	@Autowired
	private ResponseStructure<List<UserResponse>> listStructure;
	
	@Autowired
	private User user1;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	private User mapToUser(UserRequest request) {
		return User.builder()
				.userName(request.getUserName())
				.userPassword(passwordEncoder.encode(request.getUserPassword()))
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
	
	   
	
	
	private School findAdminSchool() {
		User admin = userRepo.findByUserRole(UserRole.ADMIN);
		if(admin !=null) {
			return admin.getSchool1();
		}
		else {
			throw new SchoolNotFoundException("School not found!!");
		}
		
	}
	
	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest request) {
		if(userRepo.existsByUserRole(UserRole.ADMIN))
		{
			throw new AdminAlreadyExistException("Admin already exists!!!");
		}
		else {
			User user = userRepo.save(mapToUser(request));
			userStructure.setStatusCode(HttpStatus.CREATED.value());
			userStructure.setMessage("Admin registered successfully!!!");
			userStructure.setData(mapToResponse(user));
			
	
		  return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.CREATED);	
		}
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest request) {
		School adminSchool =findAdminSchool();
		if(request.getUserRole()==UserRole.ADMIN && userRepo.existsByUserRole(UserRole.ADMIN))
		 {
			throw new AdminCannotBeRegisteredException("Only Teacher and Student can be registered!!!");
			
		 }else {
			
			
			User user = userRepo.save(mapToUser(request));
            if(request.getUserRole()==UserRole.TEACHER || request.getUserRole()==UserRole.STUDENT ) {
				user.setSchool1(adminSchool);
			}
            User savUser=userRepo.save(user);
			userStructure.setStatusCode(HttpStatus.CREATED.value());
			userStructure.setMessage("User object registered successfully!!!");
			userStructure.setData(mapToResponse(savUser));
			
	
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

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUser(int userId) {
		
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("User by this ID doesnt exist!!!"));
		userRepo.delete(user);
		user1.setDeleted(true);
		
		userStructure.setStatusCode(HttpStatus.OK.value());
		userStructure.setMessage("Data Deleted successfully!!!");
		userStructure.setData(mapToResponse(user));
		
		return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> assignUser(int userId, int programId) {
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("User not found!!"));
		AcademicProgram academicProgram = academicRepo.findById(programId).orElseThrow(() -> new AcademicProgramNorFoundException("Admin not found!!!"));
		
		if(user.getUserRole()==UserRole.ADMIN) {
			throw new AdminCannotBeAssignedToProgramException("Admin cannot be assigned ");
		}
		
		else {
			user.getAcademicProgram().add(academicProgram);
			userRepo.save(user);
			academicProgram.getUsers().add(user);
			academicRepo.save(academicProgram);
			
			
			userStructure.setStatusCode(HttpStatus.OK.value());
			userStructure.setMessage("Updated successfully!!!");
			userStructure.setData(mapToResponse(user));
			
			return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.OK);
		
			
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(int subjectId, int userId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()-> new SubjectNotFoundException("Subject not found!!"));
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("User not found!!"));
		
		if(user.getUserRole().equals(UserRole.TEACHER) && user.getSubject()==null) {
			user.setSubject(subject);
			userRepo.save(user);
			
			userStructure.setStatusCode(HttpStatus.OK.value());
			userStructure.setMessage("Added subject to the Teacher successfully!!!");
			userStructure.setData(mapToResponse(user));
			
			return new ResponseEntity<ResponseStructure<UserResponse>>(userStructure,HttpStatus.OK);
			
		}
		else {
			throw new OnlyTeacherCanBeAssignedException("Only the teacher with no subjects can be assigned to subject!!");
		}

	
	}

	
		
		



	

	

	
	
	
	
	

	

	
}
