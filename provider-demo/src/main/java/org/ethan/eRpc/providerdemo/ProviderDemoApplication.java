package org.ethan.eRpc.providerdemo;

import org.ethan.eRpc.common.util.MDCUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"**.org.ethan.**"})
public class ProviderDemoApplication {

	public static void main(String[] args) {
		MDCUtil.setTraceId();
		SpringApplication.run(ProviderDemoApplication.class, args);
	}

}
