package com.ibm.docs.test.gui.cases;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.ibm.docs.test.common.Config;
import com.ibm.docs.test.common.JUnitRerun;
import com.ibm.docs.test.gui.support.GAssert;
import com.ibm.docs.test.gui.support.JUnitTestEnv;
import com.ibm.docs.test.gui.support.LanguageSuite;
import com.ibm.docs.test.gui.support.TestEnv;

public class BaseTest {

	public Logger log = Logger.getLogger(this.getClass());
	public static final long TIMEOUT = NumberUtils.toInt(Config.getInstance()
			.get("timeout"), 600000);
	public TestEnv env = new TestEnv();

	// Enable execution continue Assert.
	@Rule
	public GAssert ga = new GAssert(env);

	// The following lines to enable test watcher.
	@Rule
	public JUnitTestEnv jenv = new JUnitTestEnv(this, env);

	// Add timeout to force to quit one case. If set timeout less than 0 in
	// config, will not run in timeout mode.
	@Rule
	public TestRule timer = new TestRule() {

		class MyTask extends java.util.TimerTask {
			private Thread mainthread = null;

			MyTask(Thread ta) {
				mainthread = ta;
			}

			@Override
			public void run() {
				log.info("Interrupt current test thread");
				mainthread.interrupt();
				env.dispose();
			}
		}

		@Override
		public Statement apply(final Statement base,
				final Description description) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					if (TIMEOUT <= 0
							|| description.getAnnotation(Test.class).timeout() > 0) {
						log.info("Execute test case in no timeout mode");
						base.evaluate();
					} else {
						final Timer timer = new Timer();
						try {
							Thread ta = Thread.currentThread();
							TimerTask task = new MyTask(ta);
							log.info("Add timer(MILLISECONDS): " + TIMEOUT);
							timer.schedule(task, TIMEOUT);
							base.evaluate();
						} finally {
							timer.cancel();
						}
					}
				}

			};

		}
	};

	@Rule
	public JUnitRerun retry = new JUnitRerun();
	
	@Rule
	public LanguageSuite languageSuite = new LanguageSuite(env);
}
