package org.ethan.eRpc.providerdemo.filter;

import org.ethan.eRpc.common.bean.ERpcThreadLocal;
import org.ethan.eRpc.common.exception.ERpcException;
import org.ethan.eRpc.core.context.ERpcRequestContext;
import org.ethan.eRpc.core.filter.ERpcFilter;
import org.ethan.eRpc.core.filter.ERpcFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class TestERpcFilter implements ERpcFilter {
	
	static final Logger logger = LoggerFactory.getLogger(TestERpcFilter.class);

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void preFilter(ERpcFilterChain chain, ERpcRequestContext context) {
		// TODO Auto-generated method stub
		try {
			context.getRequest().addAttachment("startTime", System.currentTimeMillis());
		} catch (ERpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(ERpcThreadLocal.get("traceId")+"Begin to process ERpc request : "+JSON.toJSONString(context.getRequest()));
	}

	@Override
	public void afterFilter(ERpcFilterChain chain, ERpcRequestContext context) {
		// TODO Auto-generated method stub
		logger.info("ERpc response in "+(System.currentTimeMillis()-(long)context.getRequest().getAttachment("startTime"))+"ms :"+JSON.toJSONString(context.getResponse()));
	}

}
