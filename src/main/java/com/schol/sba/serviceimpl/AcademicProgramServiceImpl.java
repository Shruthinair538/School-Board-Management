package com.schol.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.School;
import com.schol.sba.exception.SchoolNotFoundException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.requestdto.AcademicProgramRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.service.AcademicProgramService;
import com.schol.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService{
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	private AcademicProgram mapToAcademicProgram(AcademicProgramRequest request) {
		return AcademicProgram.builder()
				.programType(request.getProgramType())
				.programName(request.getProgramName())
				.beginsAt(request.getBeginsAt())
				.endsAt(request.getEndsAt())
				.build();
	}
	
	public AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram response) {
		return AcademicProgramResponse.builder()
				.programId(response.getProgramId())
				.programType(response.getProgramType())
				.programName(response.getProgramName())
				.beginsAt(response.getBeginsAt())
				.endsAt(response.getEndsAt())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicProgram(int schoolId,
			AcademicProgramRequest request) {
		School school = schoolRepo.findById(schoolId).orElseThrow(()-> new SchoolNotFoundException("School doesn't exist!!!"));
		AcademicProgram academicProgram = mapToAcademicProgram(request);
		academicProgram.setSchool(school);
		academicRepo.save(academicProgram);
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Academic programs added successfully!!!!");
		structure.setData(mapToAcademicProgramResponse(academicProgram));
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAcademicProgram(int schoolId) {
		return schoolRepo.findById(schoolId).map(school->{
			List<AcademicProgram> aList = school.getAList();
			ResponseStructure<List<AcademicProgramResponse>> rStructure=new ResponseStructure<>();
			List<AcademicProgramResponse> l=new ArrayList<>();
			
			for(AcademicProgram a:aList) {
				l.add(mapToAcademicProgramResponse(a));
			}
			
			rStructure.setStatusCode(HttpStatus.FOUND.value());
			rStructure.setMessage("Academic programs found successfully!!!!");
			rStructure.setData(l);
			return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(rStructure,HttpStatus.FOUND);
			
		}).orElseThrow(()-> new SchoolNotFoundException("School doesn't exist!!!"));
		
	}

}
