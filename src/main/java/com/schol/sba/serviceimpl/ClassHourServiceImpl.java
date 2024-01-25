package com.schol.sba.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.ClassHour;
import com.schol.sba.exception.AcademicProgramNorFoundException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.ClassHourRepository;
import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.service.ClassService;
import com.schol.sba.util.ResponseStructure;
@Service
public class ClassHourServiceImpl implements ClassService{
	
	@Autowired
	private ClassHourRepository classRepo;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	public ClassHour mapToClassHour(ClassHourRequest request ) {
		return ClassHour.builder()
				.beginsAt(request.getBeginsAt())
				.endsAt(request.getEndsAt())
				.roomNo(request.getRoomNo())
				.classStatus(request.getClassStatus())
				.build();
	}
	
	public ClassHourResponse mapToClassHourResponse(ClassHour response ) {
		return ClassHourResponse.builder()
				.classHourId(response.getClassHourId())
				.beginsAt(response.getBeginsAt())
				.endsAt(response.getEndsAt())
				.roomNo(response.getRoomNo())
				.classStatus(response.getClassStatus())
				.build();
	}


	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId,
			ClassHourRequest request) {
		AcademicProgram academicProgram = academicRepo.findById(programId).orElseThrow(()-> new AcademicProgramNorFoundException("Academic program not found!!"));
		
		
		return null;
	}

}
