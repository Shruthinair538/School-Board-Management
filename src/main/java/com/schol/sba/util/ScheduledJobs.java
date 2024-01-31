package com.schol.sba.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.School;
import com.schol.sba.enums.UserRole;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.ClassHourRepository;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.repository.UserRepository;

@Component
public class ScheduledJobs {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Autowired
	private AcademicProgramRepository academicRepo;


	@Scheduled(fixedDelay = 1000l*60)
	public void test()
	{
		deleteUserIfDeleted();
		deleteSchoolIfDeleted();
		deleteAcademicProgramIfDeleted();
	}

	private void deleteUserIfDeleted()
	{
		userRepo.findAll().stream()
		.filter(user -> !user.getUserRole().equals(UserRole.ADMIN) && user.isDeleted())
		.forEach(user ->
		{
			user.getClassHours().forEach(classHour -> classHour.setUser(null));
			classHourRepo.saveAll(user.getClassHours());

			userRepo.delete(user);
		});
	}

	private void deleteSchoolIfDeleted()
	{
		schoolRepo.findAll().stream()
		.filter(School::isDeleted)
		.forEach(school ->
		{
			school.getUList().forEach(user -> user.setSchool1(null));
			userRepo.saveAll(school.getUList());

			school.getAList().forEach(academicProgram -> academicProgram.setSchool(null));
			academicRepo.saveAll(school.getAList());

			schoolRepo.delete(school);
		});
	}

	private void deleteAcademicProgramIfDeleted()
	{
		academicRepo.findAll().stream()
		.filter(AcademicProgram::isDeleted)
		.forEach(academicProgram -> 
		{
			//Deleting All the Class Hours related to the Academic Program
			if(!academicProgram.getClassHours().isEmpty())
				classHourRepo.deleteAll(academicProgram.getClassHours());

			academicProgram.getUsers().forEach(user -> user.setAcademicProgram(null));
			userRepo.saveAll(academicProgram.getUsers());

			academicRepo.delete(academicProgram);
		});


	}
}
