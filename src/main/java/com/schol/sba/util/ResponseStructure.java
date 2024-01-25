package com.schol.sba.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.schol.sba.responsedto.SubjectResponse;
import com.schol.sba.responsedto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Component
public class ResponseStructure<T> {
	
	private int statusCode;
	private String message;
	private T data;
	
	

}
