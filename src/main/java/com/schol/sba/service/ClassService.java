package com.schol.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.schol.sba.requestdto.ClassHourDTO;
import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.util.ResponseStructure;

public interface ClassService {

	ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId);

	ResponseEntity<String> updateClassHour(List<ClassHourDTO> request);
	
	ResponseEntity<String> generateClassHoursForNext6Days();

}
