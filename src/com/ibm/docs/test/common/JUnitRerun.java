package com.ibm.docs.test.common;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * 
 * Rerun the test immediately when it's not passed
 * 
 * @author xtingx@cn.ibm.com, liuzhe@cn.ibm.com
 *
 */
public class JUnitRerun implements TestRule{
	
	private static final Logger log = Logger.getLogger(JUnitRerun.class);
	
	private int rerunCount = 0;
	
	private int rerunInterval = 0;
	
	public JUnitRerun() {
		// get rerun count from configuration
		rerunCount = NumberUtils.toInt(Config.getInstance().get("rerun"), 0);
		// get rerun interval from configuration
		rerunInterval = NumberUtils.toInt(Config.getInstance().get("rerunInterval"),0);
    }
	
	public JUnitRerun(int rerunCount) {
        this.rerunCount = rerunCount;
    }

	@Override
    public Statement apply(final Statement base, final Description description) {
        return rerunCount < 1 ? base : new Statement() {
            @Override
            public void evaluate() throws Throwable {
            	for (int i = 0; i < rerunCount; i++) {
					try {
						base.evaluate();
						return;
					} catch (AssumptionViolatedException e) {
						// Skipped, don't rerun
						throw e;
					} catch (AssertionError e) {
						log.info("Retry Again!", e);
						try {
							Thread.sleep((long) rerunInterval * 1000);
						} catch (InterruptedException re) {
						}
					} catch (Throwable t) {
						log.info("Retry Again!", t);
						try {
							Thread.sleep((long) rerunInterval * 1000);
						} catch (InterruptedException re) {
						}
					}
				}
            	
            	base.evaluate();
            }
        };
    }
    
}  