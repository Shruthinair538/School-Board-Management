package com.schol.sba.entity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.schol.sba.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;
	private boolean isDeleted;
	
	@OneToOne
	private Schedule schedule;
	
	@OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
	private List<AcademicProgram> aList=new ArrayList<>();
	
	@OneToMany(mappedBy = "school1", fetch = FetchType.EAGER)
	private List<User> uList=new ArrayList<>();
	
	
	
	
	
	
	
	 

	
	
	

	
	
	
	
}
