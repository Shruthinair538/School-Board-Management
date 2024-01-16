package com.schol.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminAlreadyExistException extends RuntimeException {
	private String message;
	
	

}
