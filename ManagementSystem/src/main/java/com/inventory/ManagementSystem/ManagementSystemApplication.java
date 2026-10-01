package com.inventory.ManagementSystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(

		info = @Info(
				title = "The Management System App",
				description = "Backend Rest APIs For Buisiness  Management System",
				version = "v1.0",
				contact = @Contact(
						name = "Mujeeb Adepoju",
						email = "adepojuadetayo677@gmail.com"

				)
		)
)
public class ManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementSystemApplication.class, args);
	}

}
