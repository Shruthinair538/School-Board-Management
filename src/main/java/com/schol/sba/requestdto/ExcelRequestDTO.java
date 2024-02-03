package com.schol.sba.requestdto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRequestDTO {
	
	private LocalDate fromDate;
	private LocalDate toDate;
	private String filePath;

}
