package org.ethan.eRpc.common.log;

import ch.qos.logback.classic.PatternLayout;

public class PatternLogbackLayout extends PatternLayout {
	static {
	    defaultConverterMap.put("traceId", TraceIdPatternConverter.class.getName());
	}
}
