package com.schol.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	
	
	

}
