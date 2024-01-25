package com.schol.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminCannotBeRegisteredException extends RuntimeException {
	
	private String message;

}
