package com.schol.sba.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.School;
import com.schol.sba.repository.SchoolRepo;
import com.schol.sba.service.SchoolService;

@Service
public class ServiceImplementation implements SchoolService{

	@Autowired
	private SchoolRepo repo;

	@Override
	public String saveSchool(int schoolId,String schoolName,long contactNo,String emailId,String address) {
		School s1=new School();
		s1.setSchoolId(schoolId);
		s1.setSchoolName(schoolName);
		s1.setContactNo(contactNo);
		s1.setEmailId(emailId);
		s1.setAddress(address);

		repo.save(s1);
		return "Data inserted successfully";

	}


//	@Override
//	public List<School> readAllData(){
//		return repo.findAll();
//	}
	
//    @Override
//	public String update(int schoolId,String schoolName,long contactNo,String emailId,String address) {
//		School s1=new School();
//		s1.setSchoolId(schoolId);
//		s1.setSchoolName(schoolName);
//		s1.setContactNo(contactNo);
//		s1.setEmailId(emailId);
//		s1.setAddress(address);
//
//		repo.save(s1);
//
//		return "Data updated successfully";
//	}

//	@Override
//	public String deleteById(int schoolId) {
//		repo.deleteById(schoolId);
//		return "Successfully Deleted";
//	}




}
