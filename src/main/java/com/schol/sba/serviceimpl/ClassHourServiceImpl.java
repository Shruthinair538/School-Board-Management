package com.schol.sba.serviceimpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.ClassHour;
import com.schol.sba.entity.Schedule;
import com.schol.sba.entity.School;
import com.schol.sba.entity.Subject;
import com.schol.sba.entity.User;
import com.schol.sba.enums.ClassStatus;
import com.schol.sba.enums.UserRole;
import com.schol.sba.exception.AcademicProgramNorFoundException;
import com.schol.sba.exception.IllegalRequestException;
import com.schol.sba.exception.InvalidClassHourException;
import com.schol.sba.exception.OnlyTeacherCanBeAssignedException;
import com.schol.sba.exception.ScheduleNotFoundException;
import com.schol.sba.exception.SubjectNotFoundException;
import com.schol.sba.exception.UserNotFoundByIdException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.ClassHourRepository;
import com.schol.sba.repository.SubjectRepository;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.requestdto.ClassHourDTO;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.service.ClassService;
import com.schol.sba.util.ResponseStructure;

import jakarta.transaction.Transactional;
@Service
public class ClassHourServiceImpl implements ClassService{

	@Autowired
	private ClassHourRepository classRepo;

	@Autowired
	private AcademicProgramRepository academicRepo;

	@Autowired
	private ResponseStructure<ClassHourResponse> structure;

	@Autowired
	private SubjectRepository subjectRepo;

	@Autowired
	private UserRepository userRepo;

	public ClassHourResponse mapToClassHourResponse(ClassHour response ) {
		return ClassHourResponse.builder()
				.classHourId(response.getClassHourId())
				.beginsAt(response.getBeginsAt())
				.endsAt(response.getEndsAt())
				.roomNo(response.getRoomNo())
				.classStatus(response.getClassStatus())
				.build();
	}

	private boolean isBreakTime(LocalDateTime beginsAt, LocalDateTime endsAt, Schedule schedule)
	{
		LocalTime breakTimeStart = schedule.getBreakTime();

		return ((breakTimeStart.isAfter(beginsAt.toLocalTime()) && breakTimeStart.isBefore(endsAt.toLocalTime())) || breakTimeStart.equals(beginsAt.toLocalTime()));
	}

	private boolean isLunchTime(LocalDateTime beginsAt, LocalDateTime endsAt , Schedule schedule)
	{
		LocalTime lunchTimeStart = schedule.getLunchTime();

		return ((lunchTimeStart.isAfter(beginsAt.toLocalTime()) && lunchTimeStart.isBefore(endsAt.toLocalTime())) || lunchTimeStart.equals(beginsAt.toLocalTime()));
	}




//	@Override
//	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId) {
//		return academicRepo.findById(programId)
//				.map(academicProgarm -> {
//					School school=academicProgarm.getSchool();
//					Schedule schedule = school.getSchedule();
//					if(schedule!=null)
//					{
//						int classHourPerDay = schedule.getClassHoursPerDay();
//						int classHourLength = (int) schedule.getClassHourLengthInMinutes().toMinutes();
//
//						LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());
//
//						LocalDateTime lunchTimeStart = LocalDateTime.now().with(schedule.getLunchTime());
//						LocalDateTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
//						LocalDateTime breakTimeStart = LocalDateTime.now().with(schedule.getBreakTime());
//						LocalDateTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
//
//
////						LocalDate date=LocalDate.now();
//						for(int i=0;i<6;i++) {
////							date.plusDays(i);
//						    
//							for(int j=1;j<=classHourPerDay+2;j++) {
//								
//								ClassHour classHour = new ClassHour();
//								LocalDateTime beginsAt = currentTime;
//								LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);
//
//								if(!isLunchTime(beginsAt, endsAt, schedule))
//								{
//									if(!isBreakTime(beginsAt, endsAt, schedule))
//									{
//										classHour.setBeginsAt(beginsAt);
//										classHour.setEndsAt(endsAt);
//										classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);
//
//										currentTime = endsAt;
//									}
//									else
//									{
//										classHour.setBeginsAt(breakTimeStart);
//										classHour.setEndsAt(breakTimeEnd);
//										classHour.setClassStatus(ClassStatus.BREAK_TIME);
//										currentTime = breakTimeEnd;
//									}
//								}
//								else
//								{
//									classHour.setBeginsAt(lunchTimeStart);
//									classHour.setEndsAt(lunchTimeEnd);
//									classHour.setClassStatus(ClassStatus.LUNCH_TIME);
//									currentTime = lunchTimeEnd;
//								}
//								classHour.setAList(academicProgarm);
//								classRepo.save(classHour);
//								structure.setStatusCode(HttpStatus.CREATED.value());
//								structure.setMessage("Class hours created successfully!!!");
//								structure.setData(mapToClassHourResponse(classHour));
//
//							}
////							currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());
//							currentTime = LocalDateTime.of(currentTime.plusDays(1).toLocalDate(), schedule.getOpensAt());
////						
//						}
//					}
//					else 
//						throw new ScheduleNotFoundException("The School does not contain any schedule!!!");
//
//					return new ResponseEntity<ResponseStructure<ClassHourResponse>>(structure,HttpStatus.CREATED);
//
//
//				})
//				.orElseThrow(() -> new AcademicProgramNorFoundException("Invalid Program Id"));
//
//
//
//	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId) {
	    return academicRepo.findById(programId)
	            .map(academicProgram -> {
	                School school = academicProgram.getSchool();
	                Schedule schedule = school.getSchedule();

	                if (schedule != null) {
	                    int classHourPerDay = schedule.getClassHoursPerDay();
	                    int classHourLength = (int) schedule.getClassHourLengthInMinutes().toMinutes();

	                    LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());
	                    
	                    LocalDateTime lunchTimeStart = LocalDateTime.now().with(schedule.getLunchTime());
						LocalDateTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
						LocalDateTime breakTimeStart = LocalDateTime.now().with(schedule.getBreakTime());
						LocalDateTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());
						
						int currentDayOfWeek = currentTime.getDayOfWeek().getValue();
	                    for (int day = 1; day <= ((7-currentDayOfWeek)+7); day++) {
	                    	
	                    	if(currentTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
	                        for (int hour = 1; hour <= classHourPerDay+2; hour++) {
	                        	
	                            ClassHour classHour = new ClassHour();
	                            LocalDateTime beginsAt = currentTime;
	                            LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);
	                            DayOfWeek dayOfWeek=beginsAt.getDayOfWeek();

	                            if (!isLunchTime(beginsAt, endsAt, schedule) && !isBreakTime(beginsAt, endsAt, schedule)) {
	                                classHour.setBeginsAt(beginsAt);
	                                classHour.setEndsAt(endsAt);
	                                classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);

	                                currentTime = endsAt;  // Move to the next time slot
	                            } else if (isBreakTime(beginsAt, endsAt, schedule)) {
	                                classHour.setBeginsAt(breakTimeStart);
	                                classHour.setEndsAt(breakTimeEnd);
	                                classHour.setClassStatus(ClassStatus.BREAK_TIME);

	                                // Skip the break and move to the next time slot
	                                currentTime = endsAt;
	                            } else {
	                                classHour.setBeginsAt(lunchTimeStart);
	                                classHour.setEndsAt(lunchTimeEnd);
	                                classHour.setClassStatus(ClassStatus.LUNCH_TIME);

	                                // Skip lunch and move to the next time slot
	                                currentTime = endsAt;
	                            }

	                            classHour.setDayOfWeek(dayOfWeek);
	                            classHour.setAList(academicProgram);
	                            
	                            classRepo.save(classHour);
	                            

	                            // Output or logging can be added here
	                        }
	                    	}
	                        // Move to the next day's opensAt time
	                        currentTime = LocalDateTime.of(currentTime.toLocalDate().plusDays(1), schedule.getOpensAt());
	                        lunchTimeStart=lunchTimeStart.plusDays(1);
	                        lunchTimeEnd=lunchTimeEnd.plusDays(1);
	                        breakTimeStart=breakTimeStart.plusDays(1);
	                        breakTimeEnd=breakTimeEnd.plusDays(1);
	                        
	                    }
	                } else {
	                    throw new ScheduleNotFoundException("The school does not contain any schedule, please provide a schedule to the school");
	                }

	                ResponseStructure<ClassHourResponse> structure = new ResponseStructure<>();
	                structure.setStatusCode(HttpStatus.CREATED.value());
	                structure.setMessage("ClassHour generated successfully for the academic program");
	                return new ResponseEntity<>(structure, HttpStatus.CREATED);
	            })
	            .orElseThrow(() -> new AcademicProgramNorFoundException("Invalid Program Id"));
	}
	
	@Override
	public ResponseEntity<String> updateClassHour(List<ClassHourDTO> listOfRequest) {
		validateClassHours(listOfRequest);

		List<ClassHour> classHoursToUpdate = new ArrayList<>();

		for (ClassHourDTO classHourDTO : listOfRequest) {
			Subject subject = subjectRepo.findById(classHourDTO.getSubjectId())
					.orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + classHourDTO.getSubjectId()));

			User teacher = userRepo
					.findById(classHourDTO.getUserId())
					.orElseThrow(() -> new UserNotFoundByIdException("User not found with id: " + classHourDTO.getUserId()));

			// Check for duplicate class hours
			if (classRepo.existsByRoomNoAndBeginsAtAndEndsAt(classHourDTO.getRoomNo(),
					classHourDTO.getBeginsAt(), classHourDTO.getEndsAt())) {
				throw new InvalidClassHourException("Duplicate class hour found for room number "
						+ classHourDTO.getRoomNo() + " at date and time " + classHourDTO.getBeginsAt());
			}

			if(teacher.getUserRole().equals(UserRole.TEACHER)) {
				ClassHour classHour = classRepo.findById(classHourDTO.getClassHourId())
						.orElseThrow(()->new IllegalRequestException("classhourId not found"));
				classHour.setSubject(subject);
				classHour.setUser(teacher);
				classHour.setRoomNo(classHourDTO.getRoomNo());
				classHour.setBeginsAt(classHourDTO.getBeginsAt());
				classHour.setEndsAt(classHourDTO.getEndsAt());
				classHour.setClassStatus(classHourDTO.getClassStatus());

				classHoursToUpdate.add(classHour);
			}
			else
				throw new OnlyTeacherCanBeAssignedException("User is not a Teacher!!");
		}

		classRepo.saveAll(classHoursToUpdate);
		return ResponseEntity.ok("Class hours updated successfully.");
	}

	private void validateClassHours(List<ClassHourDTO> classHourDTOList) {
		// Add any additional validation logic based on your requirements
		if (classHourDTOList == null || classHourDTOList.isEmpty()) {
			throw new InvalidClassHourException("Class hour list cannot be empty");
		}
	}

	@Transactional
	public ResponseEntity<String> generateClassHoursForNext6Days() {
		// Get existing ClassHour data from the database
		List<ClassHour> existingClassHours = classRepo.findAll();

		// Get the current date and time
		LocalDateTime currentDate = LocalDateTime.now();

		// Generate ClassHour instances for the next 6 days

		int day=1;

		LocalDateTime nextDay = currentDate.plusDays(day);

		// Create new instances based on existing data
		List<ClassHour> newClassHours = existingClassHours.stream()
				.map(existingClassHour -> {
					ClassHour newClassHour = new ClassHour();
					newClassHour.setBeginsAt(nextDay.withHour(existingClassHour.getBeginsAt().getHour())
							.withMinute(existingClassHour.getBeginsAt().getMinute()));
					newClassHour.setEndsAt(nextDay.withHour(existingClassHour.getEndsAt().getHour())
							.withMinute(existingClassHour.getEndsAt().getMinute()));
					newClassHour.setRoomNo(existingClassHour.getRoomNo());
					newClassHour.setClassStatus(existingClassHour.getClassStatus());

					Subject existingSubject = existingClassHour.getSubject();
					if (existingSubject != null) {
						// Reattach Subject entity to the persistence context using repository
						Subject attachedSubject = subjectRepo.findById(existingSubject.getSubjectId())
								.orElseThrow(() -> new SubjectNotFoundException("Subject not found"));
						newClassHour.setSubject(attachedSubject);
					}

					newClassHour.setAList(existingClassHour.getAList());
					newClassHour.setUser(existingClassHour.getUser());
					return newClassHour;
				})
				.collect(Collectors.toList());

		// Save the newly generated ClassHour instances
		classRepo.saveAll(newClassHours);
		return ResponseEntity.ok("Class hours auto generated successfully.");

	}

	

	//    public ResponseEntity<String> deleteClassHour(List<ClassHour> classhour){
	//
	//        for (ClassHour hour : classhour) {
	//            classRepo.delete(hour);
	//        }
	//        return ResponseEntity.ok("Class hours deleted successfully.");
	//    }
	//    



}


