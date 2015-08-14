package com.ibm.docs.test.common;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Collect failed tests and save them into suite file testspace/output/failures.txt.
 * So it's easy to rerun all failed tests by passing the file to JUnitRunner.
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class JUnitFailureCollector extends RunListener {

	private static File file = new File(Config.getInstance().get("testspace", "../testspace"), "failures.txt");

	@Override
	public void testRunStarted(Description description) throws Exception {
		FileUtils.deleteQuietly(file);
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		Description description = failure.getDescription();
		FileUtils.writeStringToFile(file, description.getClassName() + "-" + description.getMethodName() + "\n", true);
	}
}
