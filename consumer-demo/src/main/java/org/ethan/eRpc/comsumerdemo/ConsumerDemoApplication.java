package org.ethan.eRpc.comsumerdemo;

import org.ethan.eRpc.common.util.MDCUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"**.org.ethan.**"})
public class ConsumerDemoApplication {
	public static void main(String[] args) {
		MDCUtil.setTraceId();
		SpringApplication.run(ConsumerDemoApplication.class, args);
	}
}
