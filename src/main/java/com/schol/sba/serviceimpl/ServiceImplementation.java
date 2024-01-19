package com.schol.sba.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.School;
import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;
import com.schol.sba.exception.AdminAlreadyExistException;
import com.schol.sba.exception.IllegalRequestException;
import com.schol.sba.exception.InvalidUserRoleException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.requestdto.SchoolRequest;
import com.schol.sba.requestdto.SchoolResponse;
import com.schol.sba.requestdto.UserResponse;
import com.schol.sba.service.SchoolService;
import com.schol.sba.util.ResponseStructure;

@Service
public class ServiceImplementation implements SchoolService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	@Autowired
	private ResponseStructure<SchoolResponse> structure;
	
	private School mapToSchool(SchoolRequest request) {
		return School.builder()
				.schoolName(request.getSchoolName())
				.contactNo(request.getContactNo())
				.emailId(request.getEmailId())
				.address(request.getAddress())
				.build();
	}
	
	private SchoolResponse mapToSchoolResponse(School school) {
		return SchoolResponse.builder()
				.schoolId(school.getSchoolId())
				.schoolName(school.getSchoolName())
				.contactNo(school.getContactNo())
				.emailId(school.getEmailId())
				.address(school.getAddress())
				.schoolId(school.getSchoolId())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(int userId,SchoolRequest request)  {
		return userRepo.findById(userId).map(u->{
			if(u.getUserRole().equals(UserRole.ADMIN)) {
				if(u.getSchool1()==null) {
					School school=mapToSchool(request);
					school=schoolRepo.save(school);
					u.setSchool1(school);
					userRepo.save(u);
					structure.setStatusCode(HttpStatus.CREATED.value());
					structure.setMessage("School saved successfully!!!");
					structure.setData(mapToSchoolResponse(school));
					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
				}else 
					throw new IllegalRequestException("Already Exist");
			}else 
					throw new AdminAlreadyExistException("Only Admin can create School!!!!");
				
			}).orElseThrow(()-> new UserNotFoundByIdException("Failed to save School"));
	
	}

	


}
