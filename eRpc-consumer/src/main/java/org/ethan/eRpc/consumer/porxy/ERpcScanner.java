package org.ethan.eRpc.consumer.porxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.common.scanner.ClasspathPackageScanner;
import org.ethan.eRpc.common.scanner.PackageScanner;
import org.ethan.eRpc.common.util.PropertiesUtil;
import org.ethan.eRpc.consumer.annotation.EServce;
import org.ethan.eRpc.consumer.annotation.EServceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 扫描某包下所有带有eRpc注解的类
 * @author Administrator
 *
 */
@Service
public class ERpcScanner {
	
	private static final Logger logger = LoggerFactory.getLogger(ERpcScanner.class);
	
	private String[] scanPathPackages;
	
	private List<Class>eRpcAnnotations;
	
	private Map<Class,List<Class>>annotatedClasses = new ConcurrentHashMap<Class, List<Class>>();
	
	public ERpcScanner() throws ERpcException{
		if(PropertiesUtil.getConfig("eRpc.scan.package") == null ||"".equals(PropertiesUtil.getConfig("eRpc.scan.package").trim())){
			throw new ERpcException("eRpc.scan.package config is null");
		}
		eRpcAnnotations = new ArrayList<Class>(Arrays.asList(new Class[]{EServce.class,EServceClient.class}));
		scanPathPackages = PropertiesUtil.getConfig("eRpc.scan.package").trim().split(",");
		try {
			scan();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurs when scan for eRpc annotation",e);
		}
	}
	
	private void scan() throws IOException, ClassNotFoundException{
		for(String spackage : scanPathPackages){
			PackageScanner scanner = new ClasspathPackageScanner(spackage);
			List<String>classNames = scanner.getFullyQualifiedClassNameList();
			if(classNames != null && !classNames.isEmpty()){
				for(String className : classNames){
					Class c = this.getClass().getClassLoader().loadClass(className);
					for(Class ann : eRpcAnnotations){
						if(c.getAnnotation(ann) != null){
							if(annotatedClasses.get(ann) == null){
								annotatedClasses.put(ann, new LinkedList<Class>());
							}
							annotatedClasses.get(ann).add(c);
						}
					}
				}
			}
		}
	}
	
	public List<Class>getClassListWithAnnotation(Class annotation){
		return this.annotatedClasses.get(annotation);
	}
	
}
