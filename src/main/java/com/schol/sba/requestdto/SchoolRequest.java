package com.schol.sba.requestdto;

import java.util.ArrayList;
import java.util.List;

import com.schol.sba.entity.User;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class SchoolRequest {
	
	@ManyToOne
	private User user;;
	
	private String schoolName;
	private long contactNo;
	private String emailId;
	private String address;

}
