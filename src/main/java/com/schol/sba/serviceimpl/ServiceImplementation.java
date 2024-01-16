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

	


}
