package org.ethan.eRpc.core.route;

import org.ethan.eRpc.core.request.ERpcRequest;
import org.springframework.stereotype.Component;

@Component("defaultIndicator")
public class DefaultIndicator implements RouteIndicator{

	@Override
	public String getRouteIndicator(ERpcRequest request) {
		// TODO Auto-generated method stub
		return request.getHeader().getServiceName();
	}

}
