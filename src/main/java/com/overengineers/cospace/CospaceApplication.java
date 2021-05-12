package com.overengineers.cospace;

import com.overengineers.cospace.util.DatabasePopulate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class CospaceApplication {

	private final DatabasePopulate databasePopulate;

	public static void main(String[] args) {
		SpringApplication.run(CospaceApplication.class, args);
	}

	@PostConstruct
	public void populateDatabase(){
		databasePopulate.populateDatabase();
	}
}
