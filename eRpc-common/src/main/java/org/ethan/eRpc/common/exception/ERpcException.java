package org.ethan.eRpc.common.exception;

public class ERpcException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4446993299140888941L;
	
	
	public ERpcException(String msg) {
		super(msg);
	}
	
	public ERpcException(Throwable t) {
		super(t);
	}
	
	public ERpcException(String msg,Throwable t) {
		super(msg,t);
	}
}
