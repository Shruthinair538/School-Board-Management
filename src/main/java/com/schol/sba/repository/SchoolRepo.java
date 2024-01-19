package com.schol.sba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schol.sba.entity.School;
import com.schol.sba.entity.User;
import com.schol.sba.enums.UserRole;

@Repository
public interface SchoolRepo extends JpaRepository<School, Integer>   {
	
	

}
