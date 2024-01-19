package com.schol.sba.serviceimpl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.schol.sba.entity.Schedule;
import com.schol.sba.entity.School;
import com.schol.sba.exception.IllegalRequestException;
import com.schol.sba.exception.ScheduleNotFoundException;
import com.schol.sba.exception.SchoolNotFoundException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.ScheduleRepository;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.requestdto.ScheduleRequest;
import com.schol.sba.requestdto.ScheduleResponse;
import com.schol.sba.service.ScheduleService;
import com.schol.sba.util.ResponseStructure;

@Service
public class ScheduleServiceImpl implements ScheduleService{
	
	@Autowired
	private ScheduleRepository scheduleRepo;
	
	@Autowired
	private ResponseStructure<ScheduleResponse> structure;
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	private Schedule mapToSchedule(ScheduleRequest request) {
		return Schedule.builder()
				.opensAt(request.getOpensAt())
				.classHoursPerDay(request.getClassHoursPerDay())
				.closesAt(request.getClosesAt())
				.ClassHourLengthInMinutes(Duration.ofMinutes(request.getClassHourLengthInMinutes()))
				.breakTime(request.getBreakTime())
				.breakLengthInMinutes(Duration.ofMinutes(request.getBreakLengthInMinutes()))
				.lunchTime(request.getLunchTime())
				.lunchLengthInMinutes(Duration.ofMinutes(request.getBreakLengthInMinutes()))
				.build();
	}
	
	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
		
		return ScheduleResponse.builder()
				.scheduleId(schedule.getScheduleId())
				.opensAt(schedule.getOpensAt())
				.breakTime(schedule.getBreakTime())
				.lunchTime(schedule.getLunchTime())
				.closesAt(schedule.getClosesAt())
				.classHoursPerDay(schedule.getClassHoursPerDay())
				.ClassHourLengthInMinutes((int)(schedule.getClassHourLengthInMinutes().toMinutes()))
				.breakLengthInMinutes((int)(schedule.getBreakLengthInMinutes().toMinutes()))
				.lunchLengthInMinutes((int)(schedule.getLunchLengthInMinutes().toMinutesPart()))
				.build();
					
	}
	

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> addSchedule(int schoolId,ScheduleRequest request) {
		return schoolRepo.findById(schoolId).map(u->{
			if(u.getSchedule()==null) {
				Schedule schedule = mapToSchedule(request);
				schedule = scheduleRepo.save(schedule);
				u.setSchedule(schedule);
				schoolRepo.save(u);
				
				structure.setStatusCode(HttpStatus.CREATED.value());
				structure.setMessage("Scheduled!!");
				structure.setData(mapToScheduleResponse(schedule));
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED);
			}
			else
				throw new IllegalRequestException("School can have only one schedule!!");
		}).orElseThrow(()-> new UserNotFoundByIdException("Failed to save the data!!"));
		
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> getSchedule(int scheduleId) {
		Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow(()-> new UserNotFoundByIdException("User not found!!!"));
		
		structure.setStatusCode(HttpStatus.FOUND.value());
		structure.setMessage("Data Found successfully!!!");
		structure.setData(mapToScheduleResponse(schedule));
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.FOUND);
	
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateSchedule(int scheduleId, ScheduleRequest request) {
		Schedule schedule = scheduleRepo.findById(scheduleId).map(u->{
			Schedule updated=mapToSchedule(request);
			updated.setScheduleId(u.getScheduleId());
			return scheduleRepo.save(updated);
		}).orElseThrow(()->new ScheduleNotFoundException("Schedule Not Found!!"));
		
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Data Updated successfully!!!");
		structure.setData(mapToScheduleResponse(schedule));
		return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
	
		
	}
	
	

}
