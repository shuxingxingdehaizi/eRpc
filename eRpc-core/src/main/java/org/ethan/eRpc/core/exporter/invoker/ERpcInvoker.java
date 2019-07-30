package org.ethan.eRpc.core.exporter.invoker;

import org.ethan.eRpc.core.request.ERpcRequest;
import org.ethan.eRpc.core.response.ERpcResponse;

public interface ERpcInvoker {
	
	public ERpcResponse invoke(ERpcRequest request);
}
