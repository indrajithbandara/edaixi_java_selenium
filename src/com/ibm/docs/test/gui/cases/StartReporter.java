package com.ibm.docs.test.gui.cases;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

/**
 * A blank test to start reporter.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class StartReporter {
	
	@Rule
	public TestRule globalTimeout = new Timeout(10);

	@Test
	public void start() {
		//PieReporter.launch(PieReporter.class);
	}

}
