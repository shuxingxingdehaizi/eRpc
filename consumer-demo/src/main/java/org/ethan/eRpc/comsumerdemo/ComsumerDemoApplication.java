package org.ethan.eRpc.comsumerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"**.org.ethan.**"})
public class ComsumerDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ComsumerDemoApplication.class, args);
	}
}
