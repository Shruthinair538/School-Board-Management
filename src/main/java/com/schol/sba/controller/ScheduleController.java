package com.schol.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.schol.sba.requestdto.ScheduleRequest;
import com.schol.sba.responsedto.ScheduleResponse;
import com.schol.sba.service.ScheduleService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class ScheduleController {
	
	@Autowired
	private ScheduleService service;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(@PathVariable int schoolId,@RequestBody ScheduleRequest request){
		return service.addSchedule(schoolId,request);
	}
	
	@GetMapping("school/{schoolId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(@PathVariable int schoolId){
		return service.getSchedule(schoolId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("schedules/{scheduleId}")
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(@PathVariable int scheduleId,@RequestBody ScheduleRequest request){
		return service.updateSchedule(scheduleId,request);
	}
	

	

	

}
