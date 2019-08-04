package org.ethan.eRpc.consumer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示该注解的参数的参数名，
 * 默认情况下框架会使用参数的形参的名字作为参数名，若形参名字与接口参数不一致，可使用该注解
 * @author Administrator
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EParam {
	String name();
}
