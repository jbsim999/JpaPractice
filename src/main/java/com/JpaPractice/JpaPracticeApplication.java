package com.JpaPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaPracticeApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(JpaPracticeApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
