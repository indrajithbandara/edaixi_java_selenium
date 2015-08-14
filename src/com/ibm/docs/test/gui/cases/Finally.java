package com.ibm.docs.test.gui.cases;

import org.junit.Test;

/**
 * A blank test to keep reporter.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class Finally {

	@Test
	public void aWait() {
		try {
			Thread.sleep(36000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
