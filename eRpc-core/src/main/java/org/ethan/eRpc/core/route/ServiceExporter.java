package org.ethan.eRpc.core.route;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.annotation.EService;
import org.ethan.eRpc.core.bean.ServiceBean;
import org.ethan.eRpc.core.exporter.ExporterFactory;
import org.ethan.eRpc.core.exporter.ServiceExporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * 将服务暴露至远端以及本地
 * @author Admin
 *
 */
@Component
public class ServiceExporter implements InitializingBean,DisposableBean,ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	private ExporterFactory exporterFactory; 
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		exportService();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	
	private void exportService() throws ERpcException {
		Map<String,Object>controllers = applicationContext.getBeansWithAnnotation(Controller.class);
		if(controllers == null || controllers.isEmpty()) {
			
		}else {
			for(Map.Entry<String, Object>ent : controllers.entrySet()) {
				Method[] ms = ent.getClass().getMethods();
				if(ms != null && ms.length > 0) {
					for(Method m :ms) {
						if(m.isAnnotationPresent(EService.class)) {
							ServiceBean service = new ServiceBean();
							service.setName(m.getAnnotation(EService.class).name());
//							service.setParamsClasses(paramsClasses);
						}
					}
				}
				
			}
		}
		List<org.ethan.eRpc.core.exporter.ServiceExporter> exporters = exporterFactory.getExporters();
		for(org.ethan.eRpc.core.exporter.ServiceExporter export : exporters) {
			export.export(service);
		}
	}
	
}
