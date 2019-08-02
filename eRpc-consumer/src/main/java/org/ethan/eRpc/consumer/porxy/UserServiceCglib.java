package org.ethan.eRpc.consumer.porxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class UserServiceCglib implements MethodInterceptor{

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
        return obj.getClass()+"."+method.getName();
	}
	
	public static void main(String[] args) {
        UserServiceCglib cglib = new UserServiceCglib();
        UserService bookFacedImpl = (UserService) cglib.getInstance(UserService.class);
        System.out.println(bookFacedImpl.addUser());
    }

}
