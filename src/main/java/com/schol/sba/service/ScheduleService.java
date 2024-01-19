package com.schol.sba.service;

import org.springframework.http.ResponseEntity;

import com.schol.sba.requestdto.ScheduleRequest;
import com.schol.sba.requestdto.ScheduleResponse;
import com.schol.sba.util.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId, ScheduleRequest request);

	ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(int scheduleId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest request);

}
