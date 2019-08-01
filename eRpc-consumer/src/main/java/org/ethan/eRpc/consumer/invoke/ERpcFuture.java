package org.ethan.eRpc.consumer.invoke;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.ethan.eRpc.core.ERpcException;
import org.ethan.eRpc.core.response.ERpcResponse;

/**
 * 远程调用异步响应类
 * @author Admin
 *
 */
public class ERpcFuture {
	private CountDownLatch count;
	
	private long timeout;
	
	private String eRpcId;
	
	private ERpcResponse response;

	public ERpcFuture(long timeout, String eRpcId) {
		super();
		this.timeout = timeout;
		this.eRpcId = eRpcId;
		count = new CountDownLatch(1);
	}
	
	public void setResponse(ERpcResponse response) {
		this.response = response;
		count.countDown();
	}
	
	public ERpcResponse get() throws ERpcException{
		try {
			long start = System.currentTimeMillis();
			if(count.await(timeout, TimeUnit.MILLISECONDS)) {
				return response;
			}else {
				throw new ERpcException("Wait response["+eRpcId+"] for "+(System.currentTimeMillis()-start)+"ms and timeout");
			}
		} catch (InterruptedException e) {
			throw new ERpcException("Error occurs when wait remote response["+eRpcId+"]",e);
		}
	}
}
