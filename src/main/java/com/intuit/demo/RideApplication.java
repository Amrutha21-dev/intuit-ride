package com.intuit.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableKafka
@EnableTransactionManagement
public class RideApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideApplication.class, args);
	}

}
