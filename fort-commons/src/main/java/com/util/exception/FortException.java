package com.util.exception;

/**
 * 自定义异常
 * 
 * @author zhigongzhang
 *
 */
public class FortException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4334296913237595250L;
	
	public FortException(FortError error) {
		super(error.toString());
	}

}
