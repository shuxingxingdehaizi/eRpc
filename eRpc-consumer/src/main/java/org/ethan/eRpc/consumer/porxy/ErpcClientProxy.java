package org.ethan.eRpc.consumer.porxy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import org.ethan.eRpc.consumer.context.SpringContextHolder;
import org.ethan.eRpc.consumer.invoke.ERpcConsumerInvoker;
import org.springframework.context.ApplicationContext;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ErpcClientProxy implements MethodInterceptor{

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
		String serviceName = method.getName();
		String version = "1.0";
		Class returnType = method.getReturnType();
		Map<String,Object> params = null;
		if(method.getParameters() != null){
			params = new HashMap<String, Object>();
			int index = 0;
			for(Parameter p : method.getParameters()){
				params.put(p.getName(), args[index++]);
			}
		}
		ApplicationContext springApplicationContext = SpringContextHolder.getSpringApplicationContext();
		ERpcConsumerInvoker invoker = springApplicationContext.getBean(ERpcConsumerInvoker.class);
		return invoker.invoke(serviceName, version, returnType, params);
	}
	
	public static void main(String[] args) {
        ErpcClientProxy cglib = new ErpcClientProxy();
        UserService bookFacedImpl = (UserService) cglib.getInstance(UserService.class);
        System.out.println(bookFacedImpl.addUser());
    }

}
