package ru.abarigena.NauJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingTicketsApplication.class, args);
	}

}
