package com.schol.sba.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.ClassHour;
import com.schol.sba.entity.School;
import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.ClassHourRepository;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.repository.UserRepository;
import com.schol.sba.serviceimpl.ClassHourServiceImpl;

import jakarta.transaction.Transactional;

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
	
	@Autowired
	private ClassHourServiceImpl classHour;


	@Scheduled(fixedDelay = 1000l*60)
	public void test()
	{
//		deleteUserIfDeleted();
//		deleteSchoolIfDeleted();
//		deleteAcademicProgramIfDeleted();
		classHour.generateClassHoursForNext6Days();
		
	}

	private void deleteUserIfDeleted() {
	    for (User user : userRepo.findAll()) {
	        if (!UserRole.ADMIN.equals(user.getUserRole()) && Boolean.TRUE.equals(user.isDeleted())) {
	            for (ClassHour classHour : user.getClassHours()) {
	                classHour.setUser(null);
	           }
	            classHourRepo.saveAll(user.getClassHours());

	            userRepo.delete(user);
	        }
	    }
	}

	public void deleteSchoolIfDeleted() {
	    List<School> schoolsToDelete = new ArrayList<>();

	    for (School school : schoolRepo.findAll()) {
	        if (school.isDeleted()) {
	            for (User user : school.getUList()) {
	                user.setSchool1(null);
	            }
	            userRepo.saveAll(school.getUList());

	            for (AcademicProgram academicProgram : school.getAList()) {
	                academicProgram.setSchool(null);
	            }
	            academicRepo.saveAll(school.getAList());

	            schoolsToDelete.add(school);
	        }
	    }

	    schoolRepo.deleteAll(schoolsToDelete);
	}
		
	

	@Transactional
	public void deleteAcademicProgramIfDeleted()
	{
		List<AcademicProgram> academicPrograms = academicRepo.findByIsDeleted(true);
		for(AcademicProgram academicProgram:academicPrograms)
		{
			List<ClassHour> listOfClassHours = academicProgram.getClassHours();
			
			for(ClassHour listHour:listOfClassHours)
			{
				classHourRepo.delete(listHour);;
			}
			academicRepo.delete(academicProgram);

		}
	}
	}
	
	
	


	


