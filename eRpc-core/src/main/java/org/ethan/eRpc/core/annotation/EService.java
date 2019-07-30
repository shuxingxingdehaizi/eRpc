package org.ethan.eRpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * eRpc接口
 * @author Admin
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EService {
	/**
	 * 接口名字，框架会以该值将接口暴露至远端以及本地
	 * @return
	 */
	String name();
}
