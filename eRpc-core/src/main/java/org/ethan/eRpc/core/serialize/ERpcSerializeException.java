package org.ethan.eRpc.core.serialize;

public class ERpcSerializeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4934679026981443080L;

	public ERpcSerializeException(String msg){
		super(msg);
	}
	
	public ERpcSerializeException(String msg,Exception e){
		super(msg,e);
	}
	
	public ERpcSerializeException(Exception e){
		super(e);
	}
}
