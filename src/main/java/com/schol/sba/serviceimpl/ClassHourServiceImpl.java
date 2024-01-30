package com.schol.sba.serviceimpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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




	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(int programId) {
		return academicRepo.findById(programId)
				.map(academicProgarm -> {
					School school=academicProgarm.getSchool();
					Schedule schedule = school.getSchedule();
					if(schedule!=null)
					{
						int classHourPerDay = schedule.getClassHoursPerDay();
						int classHourLength = (int) schedule.getClassHourLengthInMinutes().toMinutes();

						LocalDateTime currentTime = LocalDateTime.now().with(schedule.getOpensAt());

						LocalDateTime lunchTimeStart = LocalDateTime.now().with(schedule.getLunchTime());
						LocalDateTime lunchTimeEnd = lunchTimeStart.plusMinutes(schedule.getLunchLengthInMinutes().toMinutes());
						LocalDateTime breakTimeStart = LocalDateTime.now().with(schedule.getBreakTime());
						LocalDateTime breakTimeEnd = breakTimeStart.plusMinutes(schedule.getBreakLengthInMinutes().toMinutes());

						for(int i=1;i<=6;i++) {
							for(int j=1;j<=classHourPerDay+2;j++) {
								ClassHour classHour = new ClassHour();
								LocalDateTime beginsAt = currentTime;
								LocalDateTime endsAt = beginsAt.plusMinutes(classHourLength);

								if(!isLunchTime(beginsAt, endsAt, schedule))
								{
									if(!isBreakTime(beginsAt, endsAt, schedule))
									{
										classHour.setBeginsAt(beginsAt);
										classHour.setEndsAt(endsAt);
										classHour.setClassStatus(ClassStatus.NOT_SCHEDULED);

										currentTime = endsAt;
									}
									else
									{
										classHour.setBeginsAt(breakTimeStart);
										classHour.setEndsAt(breakTimeEnd);
										classHour.setClassStatus(ClassStatus.BREAK_TIME);
										currentTime = breakTimeEnd;
									}
								}
								else
								{
									classHour.setBeginsAt(lunchTimeStart);
									classHour.setEndsAt(lunchTimeEnd);
									classHour.setClassStatus(ClassStatus.LUNCH_TIME);
									currentTime = lunchTimeEnd;
								}
								classHour.setAList(academicProgarm);
								classRepo.save(classHour);
								structure.setStatusCode(HttpStatus.CREATED.value());
								structure.setMessage("Class hours created successfully!!!");
								structure.setData(mapToClassHourResponse(classHour));

							}
							currentTime = currentTime.plusDays(1).with(schedule.getOpensAt());
						}
					}
					else 
						throw new ScheduleNotFoundException("The School does not contain any schedule!!!");

					return new ResponseEntity<ResponseStructure<ClassHourResponse>>(structure,HttpStatus.CREATED);


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
	}


