package org.ethan.eRpc.core.filter;

import org.ethan.eRpc.core.context.ERpcRequestContext;
import org.springframework.core.Ordered;

public interface ERpcFilter extends Ordered{
	public void preFilter(ERpcFilterChain chain,ERpcRequestContext context);
	
	public void afterFilter(ERpcFilterChain chain,ERpcRequestContext context);
}
