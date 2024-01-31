package com.schol.sba;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolBoardApplication.class, args);
	}

}
