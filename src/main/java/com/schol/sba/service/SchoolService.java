package com.schol.sba.service;

import java.util.List;

import com.schol.sba.entity.School;

public interface SchoolService {
	
	String saveSchool(int schoolId,String schoolName,long contactNo,String emailId,String address);

//	List<School> readAllData();
//
//	String deleteById(int schoolId);
//
//	String update(int schoolId, String schoolName, long contactNo, String emailId, String address);


}
