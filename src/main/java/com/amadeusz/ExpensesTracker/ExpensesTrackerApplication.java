package com.amadeusz.ExpensesTracker;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ExpensesTrackerApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load(); // Ładowanie .env
        System.setProperty("POSTGRES_DB_TEST", dotenv.get("POSTGRES_DB_TEST"));
        System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
        System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));

		SpringApplication.run(ExpensesTrackerApplication.class, args);
	}

}
