package org.ethan.eRpc.core.route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ethan.eRpc.core.annotation.EService;
import org.ethan.eRpc.core.context.SpringContextHolder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class ERpcRequestRouter implements InitializingBean,DisposableBean{
	
	/**
	 * 
	 */
	private ConcurrentHashMap<String, ControllerMethod>routMap;
	
	@Autowired
	private SpringContextHolder springContextHolder;
	
	private ApplicationContext springContext;
	
	public ControllerMethod getControllerMethod(String indicator) {
		return routMap == null ? null : routMap.get(indicator);
	}
	
	public class ControllerMethod{
		Object controllerBean;
		
		Method method;

		public ControllerMethod(Object controllerBean, Method method) {
			super();
			this.controllerBean = controllerBean;
			this.method = method;
		}

		public Object getControllerBean() {
			return controllerBean;
		}

		public void setControllerBean(Object controllerBean) {
			this.controllerBean = controllerBean;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		springContext = springContextHolder.getSpringApplicationContext();
	} 
	
	

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		this.routMap.clear();
	}
	
	
}
