package com.schol.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SchoolNotFoundException extends RuntimeException{
	
	private String message;

}
