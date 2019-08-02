package org.ethan.eRpc.consumer.porxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
/**
 * 为@EService接口生成动态代理类,并加入spring容器，使用者可以使用autowrite注解使用接口
 * @author Admin
 * @see https://blog.csdn.net/liuyueyi25/article/details/83244255
 * @see https://www.cnblogs.com/juncaoit/p/7591778.html
 */
@Configuration
public class ERpcPoxyCreator implements BeanDefinitionRegistryPostProcessor {


	@Override
	 // 注册Bean实例，使用supply接口, 可以创建一个实例，并主动注入一些依赖的Bean；当这个实例对象是通过动态代理这种框架生成时，就比较有用了
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		 BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserService.class, () -> {
			 	UserServiceCglib cglib = new UserServiceCglib();
		        UserService bookFacedImpl = (UserService) cglib.getInstance(UserService.class);
		        return bookFacedImpl;
	        });
        ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("userService", builder.getRawBeanDefinition());
	}
	
	
	
	

	@Override
	// 注册Bean定义，容器根据定义返回bean
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub
//		//构造bean定义
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
//                .genericBeanDefinition(BeanTestService.class);
//        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
//        //注册bean定义
//        registry.registerBeanDefinition("beanTestService", beanDefinition);
	}

}
