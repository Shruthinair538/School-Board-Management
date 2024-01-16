package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.schol.sba.entity.School;
import com.schol.sba.service.SchoolService;

@Controller
public class SchoolController {
	
	@Autowired
	private SchoolService schoolService;
	
	
	
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam int schoolId,@RequestParam  String schoolName,@RequestParam long contactNo,@RequestParam String emailId,@RequestParam String address) {
		return schoolService.saveSchool(schoolId,schoolName,contactNo,emailId,address);
	}
	
	
//	@ResponseBody
//	@RequestMapping(value = "/readAll",method = RequestMethod.GET)
//	public List<School> readAll(){
//		return schoolService.readAllData();
//	}
	
	
//	@ResponseBody
//	@RequestMapping(value = "/update",method = RequestMethod.POST)
//	public String updateById(@RequestParam int schoolId,@RequestParam String schoolName,@RequestParam long contactNo,@RequestParam String emailId,@RequestParam String address) {
//		return schoolService.update(schoolId,schoolName,contactNo,emailId,address);
//		 
//	}
	
	//Delete
//	@ResponseBody
//	@RequestMapping(value = "/delete",method = RequestMethod.GET)
//	public String delete(@RequestParam int schoolId) {
//		return schoolService.deleteById(schoolId);
//		
//	}
	
	
	

}
