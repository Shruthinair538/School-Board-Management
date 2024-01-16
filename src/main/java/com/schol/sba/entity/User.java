package com.schol.sba.entity;

import com.schol.sba.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	
	
	
	
	
	

}
