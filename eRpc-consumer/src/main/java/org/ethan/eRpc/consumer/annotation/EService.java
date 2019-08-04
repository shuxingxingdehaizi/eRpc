package org.ethan.eRpc.consumer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 框架会把使用EServiceClient注解的interface内的所有方法映射为eRpc接口调用
 * 默认情况下，eRpc接口名为方法名，版本号为1.0，超时时间为10s
 * 如果方法名需要和eRpc借口名不一样，或者版本号不为1.0，或者超时时间不为10s时，使用该注解定制
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EService {
	/**
	 * eRpc接口名
	 * @return
	 */
	String name();
	
	/**接口版本号
	 * 
	 */
	String version();
	
	/**
	 * 调用超时时间
	 * @return
	 */
	long timeOut();
}
