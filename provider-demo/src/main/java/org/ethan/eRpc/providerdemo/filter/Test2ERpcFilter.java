package org.ethan.eRpc.providerdemo.filter;

import org.ethan.eRpc.core.context.ERpcRequestContext;
import org.ethan.eRpc.core.filter.ERpcFilter;
import org.ethan.eRpc.core.filter.ERpcFilterChain;
import org.springframework.stereotype.Service;

@Service
public class Test2ERpcFilter implements ERpcFilter {

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void preFilter(ERpcFilterChain chain, ERpcRequestContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFilter(ERpcFilterChain chain, ERpcRequestContext context) {
		// TODO Auto-generated method stub

	}

}
