package com.infinityinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class InfinityInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfinityInsuranceApplication.class, args);
	}

}
