package com.ibm.docs.test.common;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

/**
 * A JUnit suite with parallel test capability.
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class JUnitSuite extends ParentRunner<Runner> {
	
	protected List<Runner> runners;
	
	protected ExecutorService executorService;

	protected String name;
	
	/**
	 * Construct a JUnit suite
	 * @param name suite name
	 * @param runners the runners should be included in the suite
	 * @param thread thread numbers to parallelize testing
	 * @throws InitializationError
	 */
	@SuppressWarnings("all")
	protected JUnitSuite(String name, List<Runner> runners, int thread) throws InitializationError {
		super(null);
		this.name = name;
		this.runners = runners;
		if (thread > 1) {
			executorService = Executors.newFixedThreadPool(thread);	
			RunnerScheduler runnerScheduler = new RunnerScheduler() {
				
				public void schedule(Runnable childStatement) {
					executorService.submit(childStatement);
				}

				public void finished() {
					
				}
			};
			for (Runner runner : runners) {
				if (runner instanceof ParentRunner) 
					((ParentRunner<?>) runner).setScheduler(runnerScheduler);
			}
		}
	}
	
	@Override
	protected List<Runner> getChildren() {
		return runners;
	}

	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}


	@Override
	protected String getName() {
		return this.name;
	}

	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
		if (executorService != null) {
			try {
				executorService.shutdown();
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	@Override
	protected void runChild(Runner runner, final RunNotifier notifier) {
		runner.run(notifier);	
	}
}