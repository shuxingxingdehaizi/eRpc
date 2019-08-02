package org.ethan.eRpc.core.route;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ethan.eRpc.common.bean.Host;
import org.ethan.eRpc.common.bean.ServiceBean;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.core.annotation.EService;
import org.ethan.eRpc.core.exporter.ExporterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
	static final Logger logger = LoggerFactory.getLogger(ServiceExporter.class);

	private ApplicationContext applicationContext;
	
	@Autowired
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
			List<org.ethan.eRpc.core.exporter.ServiceExporter> exporters = exporterFactory.getExporters();
			for(Map.Entry<String, Object>ent : controllers.entrySet()) {
				Method[] ms = ent.getValue().getClass().getMethods();
				if(ms != null && ms.length > 0) {
					for(Method m :ms) {
						if(m.isAnnotationPresent(EService.class)) {
							EService serviceConfig = m.getAnnotation(EService.class);
							ServiceBean service = new ServiceBean();
							service.setName(serviceConfig.name());
							service.setTimeOut(serviceConfig.timeOut());
							service.setVersion(serviceConfig.verion());
							service.setServiceMethod(m);
							service.setClassName(ent.getValue().getClass().getName());
							service.setBeanName(ent.getKey());
							Parameter[] ps = m.getParameters();
							if(ps != null && ps.length > 0) {
								List<ServiceBean.Param>params = new ArrayList<ServiceBean.Param>();
								for(Parameter p : ps) {
									params.add(new ServiceBean.Param(p.getName(),p.getClass().getName()));
								}
								service.setParams(params);
							}
							
							InetAddress addr;
							try {
								addr = InetAddress.getLocalHost();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								logger.error("Error occurs when getLocalHost",e);
								throw new ERpcException("Error occurs when getLocalHost",e);
							}
							Host provider = new Host(); 
							provider.setApplicationName(PropertiesUtil.getConfig("applicationName"));
							provider.setHostName(addr.getHostName().toString());
							provider.setIp(addr.getHostAddress().toString());
							service.addProvider(provider);
							
							for(org.ethan.eRpc.core.exporter.ServiceExporter export : exporters) {
								export.export(service);
							}
						}
					}
				}
				
			}
		}
		
		
	}
	
}
