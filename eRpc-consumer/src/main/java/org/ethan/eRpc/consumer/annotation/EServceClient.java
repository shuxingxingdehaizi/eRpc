package org.ethan.eRpc.consumer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在interface上使用改注解，代表改接口为eRpc接口。
 * 框架会解析该interface，使用动态代理生成其实现类，并进行eRpc调用
 * @author Administrator
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EServceClient {
	
}
