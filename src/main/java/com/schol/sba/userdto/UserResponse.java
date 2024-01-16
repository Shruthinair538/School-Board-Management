package com.schol.sba.userdto;

import com.schol.sba.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private int userId;
	private String userName;
	private String userFirstName;
	private String userLastName;
	private long userContactNo;
	private String userEmail;
	private UserRole userRole;

}
