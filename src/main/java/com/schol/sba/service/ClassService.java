package com.schol.sba.service;

import org.springframework.http.ResponseEntity;

import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.util.ResponseStructure;

public interface ClassService {

	ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId, ClassHourRequest request);

}
