package com.ibm.docs.test.gui.support;

import org.apache.log4j.Logger;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.ibm.docs.test.common.GuideLink;
import com.ibm.docs.test.common.JUnitExtra;
import com.ibm.docs.test.common.Since;
import com.ibm.docs.test.common.TestDoc;
import com.ibm.docs.test.common.TestLink;

/**
 * Integrate TestEnv feature with JUnit
 * 
 * @author liuzhe@cn.ibm.com, xinyutan@cn.ibm.com
 * 
 */
public class JUnitTestEnv implements TestRule {

	protected static final Logger log = Logger.getLogger(JUnitTestEnv.class);

	private Object target = null;

	private TestEnv env;

	public JUnitTestEnv(Object target, TestEnv env) {
		this.target = target;
		this.env = env;
	}

	/**
	 * Invoked when test is starting
	 * 
	 * @param description
	 */
	protected void starting(Description description) {
		// Create temporary & output directory the test
		String testName = getTestName(description);
		env.setTempDir(testName + "/.temp");
		env.setOutputDir(testName);
		env.start();
		log.info(String.format("Start %s", testName));
		GuideLink guideLink = description.getTestClass().getAnnotation(
				GuideLink.class);
		if (guideLink != null)
			log.info(String.format("GVT Guide link: %s", guideLink.value()));
		TestDoc testDoc = description.getAnnotation(TestDoc.class);
		if (testDoc != null)
			log.info(String.format("Test document: %s", testDoc.value()));
		TestLink testLink = description.getAnnotation(TestLink.class);
		if (testLink != null)
			log.info(String.format("Test notes link: %s", testLink.value()));
		Since since = description.getAnnotation(Since.class);
		if (since != null)
			log.info(String.format("Since build: %s", since.value()));
		env.initDriveBy(target);
	}

	/**
	 * Invoked when assertion is failed
	 * 
	 * @param e
	 * @param description
	 */
	protected void failed(AssertionError e, Description description) {
		log.error(String.format("Failure!", getTestName(description)), e);
		// take screen shot for reference
		env.takeScreenShot("failure.");
		// add to reporter
		//PieModel.addFailureList(getTestName(description));
	}

	/**
	 * Invoked when an exception is thrown
	 * 
	 * @param e
	 * @param description
	 */
	protected void error(Throwable e, Description description) {
		log.error(String.format("Error!", getTestName(description)), e);
		// take screen shot for reference
		env.takeScreenShot("error.");
		// add to reporter
		//PieModel.addErrorList(getTestName(description));
	}

	/**
	 * Invoked when test is skipped because the test is not meaningful in the
	 * context. The test should not be counted as a failure.
	 * 
	 * @param e
	 * @param description
	 */
	protected void skipped(AssumptionViolatedException e,
			Description description) {
		log.info(String.format("Skipped!", getTestName(description)), e);
	}

	/**
	 * Invoked when test
	 * 
	 * @param description
	 */
	protected void passed(Description description) {
		log.info(String.format("Passed!", getTestName(description)));
		// add to reporter
		//PieModel.addPassedList(getTestName(description));
	}

	/**
	 * Always invoked no matter what result test is.
	 * 
	 * @param description
	 */
	protected void finished(Description description) {
		// this properties maybe used by reporter.
		JUnitExtra.setExtra(env.outputProperties);
		// dispose test environment to release resources after testing
		// automatically
		env.dispose();
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					starting(description);
					base.evaluate();
					passed(description);
				} catch (AssumptionViolatedException e) {
					skipped(e, description);
					throw e;
				} catch (AssertionError e) {
					failed(e, description);
					throw e;
				} catch (MultipleFailureException e) {
					// add to reporter
					//PieModel.addFailureList(getTestName(description));
					throw e;
				} catch (Throwable e) {
					error(e, description);
					throw e;
				} finally {
					finished(description);
				}
			}
		};
	}

	protected String getTestName(Description description) {
		if (env.gLanguage == null)
			return description.getClassName() + "-"
					+ description.getMethodName();
		else
			return description.getClassName() + "-"
					+ description.getMethodName() + "[@" + env.gLanguage + "]";
	}
}
