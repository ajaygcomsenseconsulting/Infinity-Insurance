package com.infinityinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer

public class InfinityInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfinityInsuranceApplication.class, args);
	}

}
