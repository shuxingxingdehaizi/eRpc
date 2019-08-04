package org.ethan.eRpc.consumer.porxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.ethan.eRpc.consumer.annotation.EParam;
import org.ethan.eRpc.consumer.annotation.EService;
import org.ethan.eRpc.consumer.context.SpringContextHolder;
import org.ethan.eRpc.consumer.invoke.ERpcConsumerInvoker;
import org.springframework.context.ApplicationContext;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ErpcClientProxy implements MethodInterceptor{
	
	private static final String DEFAULT_VERSION = "1.0";

	private Object target;

    public Object getInstance(Class type) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        // 设置回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

	    
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		//拦截被执行方法，执行调用
		String serviceName = getServiceName(method);
		String version = getVersion(method);
		Class returnType = method.getReturnType();
		Map<String,Object> params = null;
		if(method.getParameters() != null){
			params = new HashMap<String, Object>();
			int index = 0;
			for(Parameter p : method.getParameters()){
				params.put(getParamName(p), args[index++]);
			}
		}
		return SpringContextHolder
				.getSpringApplicationContext()
				.getBean(ERpcConsumerInvoker.class)
				.invoke(serviceName, version, returnType, params);
	}
	
	/**
	 * 获取接口名
	 * @return
	 */
	private String getServiceName(Method method){
		EService eService = method.getAnnotation(EService.class);
		if(eService != null){
			return eService.name();
		}
		return method.getName();
	}
	/**
	 * 获取接口版本号
	 * @return
	 */
	private String getVersion(Method method){
		EService eService = method.getAnnotation(EService.class);
		if(eService != null){
			return eService.version();
		}
		return DEFAULT_VERSION;
	}
	/**
	 * 获取参数名
	 * @return
	 */
	private String getParamName(Parameter p){
		EParam eParam = p.getAnnotation(EParam.class);
		if(eParam != null){
			return eParam.name();
		}
		return p.getName();
	}

}
