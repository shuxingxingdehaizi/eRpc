package org.ethan.eRpc.core.filter;

import java.util.List;

import org.ethan.eRpc.core.context.ERpcRequestContext;

public class ERpcFilterChain {
	private List<ERpcFilter> filters;
	
	private int currentIndex = 0;
	
	
	
	public ERpcFilterChain(List<ERpcFilter> filters) {
		super();
		this.filters = filters;
	}



	public void doPreFilter(ERpcFilterChain chain,ERpcRequestContext context) {
		if(currentIndex < filters.size()) {
			filters.get(currentIndex++).preFilter(chain, context);
		}
	}
	
	public void doAfterFilter(ERpcFilterChain chain,ERpcRequestContext context) {
		if(currentIndex >= 0) {
			filters.get(currentIndex--).afterFilter(chain, context);
		}
	}
}
