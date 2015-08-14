package com.ibm.docs.test.gui.support;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.hamcrest.Matcher;
import org.junit.rules.Verifier;
import org.junit.runners.model.MultipleFailureException;

/**
 * A set of assertion methods useful for writing tests, allows execution of a
 * test to continue after the first problem is found.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class GAssert extends Verifier {

	private static final Logger log = Logger.getLogger(GAssert.class);

	private List<Throwable> errors = new ArrayList<Throwable>();

	private TestEnv env;

	public GAssert(TestEnv env) {
		this.env = env;
	}

	@Override
	protected void verify() throws Throwable {
		if (!errors.isEmpty()) {
			throw new MultipleFailureException(errors);
		}
	}

	/**
	 * Adds a Throwable to the table. Execution continues, but the test will
	 * fail at the end.
	 */
	private void addError(Throwable error) {
		errors.add(error);
	}

	/**
	 * Asserts that {@code actual} satisfies the condition specified by
	 * {@code matcher}. If not, adds a failure to the table. Execution
	 * continues, but the test will fail at the end if the match fails.
	 * 
	 * @param actual
	 *            actual value
	 * @param matcher
	 *            an expression, built of {@code Matcher}s, specifying allowed
	 *            values
	 */
	public <T> void assertThat(final T actual, final Matcher<T> matcher) {
		assertThat("", actual, matcher);
	}

	/**
	 * Asserts that {@code actual} satisfies the condition specified by
	 * {@code matcher}. If not, adds a failure with the given {@code message} to
	 * the table. Execution continues, but the test will fail at the end if the
	 * match fails.
	 * 
	 * @param message
	 *            the identifying error message
	 * @param actual
	 *            actual value
	 * @param matcher
	 *            an expression, built of {@code Matcher}s, specifying allowed
	 *            values
	 */
	public <T> void assertThat(final String message, final T actual,
			final Matcher<T> matcher) {
		checkSucceeds(new Callable<Object>() {
			public Object call() throws Exception {
				Assert.assertThat(message, actual, matcher);
				return actual;
			}
		});
	}

	/**
	 * Asserts that two objects are equal. If they are not, adds a failure to
	 * the table. Execution continues, but the test will fail at the end if the
	 * match fails.
	 * 
	 * @param expected
	 *            expected value
	 * @param actual
	 *            actual value
	 */
	public void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	/**
	 * Asserts that two objects are equal. If they are not, adds a failure with
	 * the given {@code message} to the table. Execution continues, but the test
	 * will fail at the end if the match fails.
	 * 
	 * @param message
	 *            the identifying error message
	 * @param expected
	 *            expected value
	 * @param actual
	 *            actual value
	 */
	public void assertEquals(final String message, final Object expected,
			final Object actual) {
		checkSucceeds(new Callable<Object>() {
			public Object call() throws Exception {
				Assert.assertEquals(message, expected, actual);
				return actual;
			}
		});
	}

	/**
	 * Asserts that a condition is true. If it isn't, adds a failure to the
	 * table. Execution continues, but the test will fail at the end if the
	 * match fails.
	 * 
	 * @param condition
	 *            condition to be checked
	 */
	public void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}

	/**
	 * Asserts that a condition is true. If it isn't, adds a failure with the
	 * given {@code message} to the table. Execution continues, but the test
	 * will fail at the end if the match fails.
	 * 
	 * @param message
	 *            the identifying message
	 * @param condition
	 *            condition to be checked
	 */
	public void assertTrue(final String message, final boolean condition) {
		checkSucceeds(new Callable<Object>() {
			public Object call() throws Exception {
				Assert.assertTrue(message, condition);
				return condition;
			}
		});
	}

	/**
	 * Asserts that a condition is false. If it isn't, adds a failure to the
	 * table. Execution continues, but the test will fail at the end if the
	 * match fails.
	 * 
	 * @param condition
	 *            condition to be checked
	 */
	public void assertFalse(boolean condition) {
		assertFalse(null, condition);
	}

	/**
	 * Asserts that a condition is false. If it isn't, adds a failure with the
	 * given {@code message} to the table. Execution continues, but the test
	 * will fail at the end if the match fails.
	 * 
	 * @param message
	 *            the identifying message
	 * @param condition
	 *            condition to be checked
	 */
	public void assertFalse(String message, boolean condition) {
		assertTrue(message, !condition);
	}

	/**
	 * Adds to the table the exception, if any, thrown from {@code callable}.
	 * Execution continues, but the test will fail at the end if
	 * {@code callable} threw an exception.
	 */
	private Object checkSucceeds(Callable<Object> callable) {
		try {
			return callable.call();
		} catch (Throwable e) {
			addError(e);
			failed(e);
			return null;
		}
	}

	private void failed(Throwable e) {
		log.error("Failure!", e);
		env.takeScreenShot("failure.");
	}
}