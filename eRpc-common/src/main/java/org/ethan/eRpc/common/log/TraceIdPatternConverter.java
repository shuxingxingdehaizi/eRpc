package org.ethan.eRpc.common.log;

import org.ethan.eRpc.common.bean.ERpcThreadLocal;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TraceIdPatternConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		// TODO Auto-generated method stub
		return (String) ERpcThreadLocal.get("traceId");
	}

}
