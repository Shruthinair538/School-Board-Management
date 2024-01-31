package com.schol.sba.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.schol.sba.entity.School;
import com.schol.sba.enums.UserRole;
import com.schol.sba.exception.AdminAlreadyExistException;
import com.schol.sba.exception.IllegalRequestException;
import com.schol.sba.exception.SchoolNotFoundException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.requestdto.SchoolRequest;
import com.schol.sba.responsedto.SchoolResponse;
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
	
	@Autowired
	private ResponseStructure<List<SchoolResponse>> resStructure;

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
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest request)  {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByUserName(userName).map(u->{
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

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest request) {
		School school=schoolRepo.findById(schoolId).map(s ->{
			School school2 = mapToSchool(request);
			school2.setSchoolId(schoolId);
			return schoolRepo.save(school2);
		}).orElseThrow(()-> new SchoolNotFoundException("School not found!!!"));

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("School updated successfully!!!");
		structure.setData(mapToSchoolResponse(school));
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);



	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId) {
		School school = schoolRepo.findById(schoolId).orElseThrow(()-> new SchoolNotFoundException("School not found!!!"));

		structure.setStatusCode(HttpStatus.FOUND.value());
		structure.setMessage("School found successfully!!!");
		structure.setData(mapToSchoolResponse(school));
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.FOUND);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchools() {
		List<School> list = schoolRepo.findAll();

		List<SchoolResponse> schoolResponse=list.stream()
				.map(u->mapToSchoolResponse(u))
				.collect(Collectors.toList());


		resStructure.setStatusCode(HttpStatus.FOUND.value());
		resStructure.setMessage("Fetched all schools successfully!!!!");
		resStructure.setData(schoolResponse);
		return new ResponseEntity<ResponseStructure<List<SchoolResponse>>>(resStructure,HttpStatus.FOUND);


	}

	
	//soft delete:it marks the row as deleted by setting it to 1.
	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		School school = schoolRepo.findById(schoolId).orElseThrow(()-> new SchoolNotFoundException("School not found!!!"));
       
        school.setDeleted(true);
        schoolRepo.save(school);
        
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("School deleted successfully!!!");
		structure.setData(mapToSchoolResponse(school));
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
	}




}
