package org.ethan.eRpc.consumer.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware{
	
	private static ApplicationContext springApplicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.springApplicationContext = applicationContext;
	}

	public static ApplicationContext getSpringApplicationContext() {
		return springApplicationContext;
	}
}
