package com.ibm.docs.test.gui.support;

/**
 * Represent an exception for test env
 * @author liuzhe@cn.ibm.com
 *
 */
public class TestEnvExcepiton extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TestEnvExcepiton() {
		super();
	}

	public TestEnvExcepiton(String message, Throwable cause) {
		super(message, cause);
	}

	public TestEnvExcepiton(String message) {
		super(message);
	}

	public TestEnvExcepiton(Throwable cause) {
		super(cause);
	}
}
