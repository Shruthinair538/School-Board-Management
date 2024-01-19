package com.schol.sba.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.schol.sba.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(unique = true)
	private String userName;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private long userContactNo;
	@Column(unique = true)
	private String userEmail;
	private UserRole userRole;
	private boolean isDeleted;
	
	@ManyToOne
	private School school1;
	
	@ManyToMany
	private List<AcademicProgram> academicProgram=new ArrayList<>();
	
	
	
	
	
	
	
	
	

}
