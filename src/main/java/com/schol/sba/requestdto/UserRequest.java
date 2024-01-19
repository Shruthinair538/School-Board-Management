package com.schol.sba.requestdto;



import com.schol.sba.enums.UserRole;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	
	
	@NotNull(message = "Username cannot be null")
	@Pattern(regexp = "^[a-zA-Z0-9]+$" ,message = "Invalid Username!!!")
	private String userName;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
    @Max(999999999)
    @Min(999999999)
	private long userContactNo;
	private String userEmail;
	private UserRole userRole; 
	
	

}
