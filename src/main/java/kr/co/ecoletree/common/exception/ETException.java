package kr.co.ecoletree.common.exception;

public class ETException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ETException() {
		super();
	}
	
	ETException(String msg) {
		super(msg);
	}
}
