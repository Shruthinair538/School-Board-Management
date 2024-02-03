package com.schol.sba.serviceimpl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.AcademicProgram;
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
	private ResponseStructure<List<ClassHour>> listStructure;

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
									classHour.setAcademicProgram(academicProgram);

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


	@Override
	public ResponseEntity<ResponseStructure<List<ClassHour>>> createClassHourForNextWeek(int programId) {

		AcademicProgram academicProgram = academicRepo.findById(programId).get();

		List<ClassHour> originalClassHours  = academicProgram.getClassHours();
		List<ClassHour> newClassHours = new ArrayList<>();

		originalClassHours.forEach((cl) -> {
			ClassHour createNewClassHour = createNewClassHour(cl);
			newClassHours.add(createNewClassHour);

		});

		newClassHours.forEach((hour) -> {
			LocalDateTime plusDays = hour.getBeginsAt().plusDays(7);
			hour.setBeginsAt(plusDays);
			classRepo.save(hour);

		});

		listStructure.setData(null);
		listStructure.setMessage("New Class Hour Created For Next Week");
		listStructure.setStatusCode(HttpStatus.CREATED.value());

		return new ResponseEntity<ResponseStructure<List<ClassHour>>>(listStructure, HttpStatus.CREATED);

	}


	@Override
	public ClassHour createNewClassHour(ClassHour cl) {
		ClassHour classHour2 = new ClassHour();

		classHour2.setAcademicProgram(cl.getAcademicProgram());
		classHour2.setBeginsAt(cl.getBeginsAt());
		classHour2.setClassStatus(cl.getClassStatus());
		classHour2.setEndsAt(cl.getEndsAt());
		classHour2.setRoomNo(cl.getRoomNo());
		classHour2.setSubject(cl.getSubject());
		classHour2.setUser(cl.getUser());

		return classHour2;

	}


	private List<ClassHour> generateClassHoursForWeek(AcademicProgram program, LocalDate startingDate) {

		List<ClassHour> classHours = new ArrayList<>();
		Schedule schedule = program.getSchool().getSchedule();
		Duration classDuration = schedule.getClassHourLengthInMinutes();
		Duration lunchDuration = schedule.getLunchLengthInMinutes();
		Duration breakDuration = schedule.getBreakLengthInMinutes();
		LocalTime breakTime = schedule.getBreakTime();
		LocalTime lunchTime = schedule.getLunchTime();
		Duration topUp = Duration.ofMinutes(2);

		// Find the next Monday from the starting date
		LocalDate nextMonday = startingDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

		for (int dayOfWeek = 0; dayOfWeek < 6; dayOfWeek++) { // Iterate from Monday to Saturday

			LocalDate currentDate = nextMonday.plusDays(dayOfWeek);
			LocalTime opensAt = schedule.getOpensAt();
			LocalTime endsAt = opensAt.plus(classDuration);
			ClassStatus status = ClassStatus.NOT_SCHEDULED;

			for (int classPerDay = schedule.getClassHoursPerDay(); classPerDay > 0; classPerDay--) {
				ClassHour classHour = ClassHour.builder()
						.beginsAt(LocalDateTime.of(currentDate, opensAt))
						.endsAt(LocalDateTime.of(currentDate, endsAt))
						.roomNo(10)
						.classStatus(status)
						.academicProgram(program)
						.build();

				classHours.add(classHour);

				if (breakTime.isAfter(opensAt.minus(topUp)) && breakTime.isBefore(endsAt.plus(topUp))) {
					opensAt = opensAt.plus(breakDuration);
					endsAt = endsAt.plus(breakDuration);
				} else if (lunchTime.isAfter(opensAt.minus(topUp)) && lunchTime.isBefore(endsAt.plus(topUp))) {
					opensAt = opensAt.plus(lunchDuration);
					endsAt = endsAt.plus(lunchDuration);
				}

				opensAt = endsAt;
				endsAt = opensAt.plus(classDuration);
			}
		}

		return classHours;
	}

}










