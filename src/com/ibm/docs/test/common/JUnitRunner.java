package com.ibm.docs.test.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.InitializationError;

/**
 * The class provide a command line interface to run JUnit test with more powerful customization capability
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class JUnitRunner {
	private static final Logger log = Logger.getLogger(JUnitRunner.class);
	private static final HashSet<String> ALL_METHODS = new HashSet<String>(0);
	private static final String USAGE = "Usage:\n" + 
		"run [options] <[test definition] | [test suite file]>\n" +
		"Options\n" +
		"	-D<property>=<value>	Set system property.\n" +
		"	-r	Set a runnable which will be executed before all tests start.\n" +
		"	-l	Set a test listeners.\n" +
		"	-n	Assign a suite name.\n" +
		"	-help	Print usage.\n" +
		"	-p Thread number to parallelize execution." +
		"Test Definition\n" +
		"	-tp	<package name>	Add all classes in the package to test.\n" +
		"	-tc	<class name>[-<method name>]	Add classes to test.\n";
	
	/**
	 * Print usage
	 * @param msg
	 * @param code
	 */
	private static void printUsage(String msg, int code) {
		if (msg != null)
			System.out.println(msg);
		System.out.print(USAGE);
		System.exit(code);
	}
	
	
	public static void main(String... args) {
		ArrayList<String> runnableClasses = new ArrayList<String>();
		ArrayList<String> listenerClasses = new ArrayList<String>();
		ArrayList<String> tcs = new ArrayList<String>();
		ArrayList<String> tps = new ArrayList<String>();
		String suiteName = "Unnamed";
		String suiteFile = null;
		int thread = 0;
		// Parse the arguments
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if ("-help".equals(arg)) {
				printUsage(null, 0);
			} else if (arg.startsWith("-D")) {
				String propEntry = arg.substring(2);
				String key = propEntry;
				String value = null;
				int in = propEntry.indexOf("=");
				if (in >= 0) {
					key = propEntry.substring(0, in);
					value = propEntry.substring(++in);
				}
				System.setProperty(key, value);
			} else if (arg.equals("-p")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				try {
					thread = Integer.parseInt(args[i]);
				} catch(Exception e) {
					printUsage("The value must an integer for -p option.", 1);
				}
			} else if (arg.equals("-r")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				runnableClasses.add(args[i]);
			} else if (arg.equals("-l")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				listenerClasses.add(args[i]);
			} else if (arg.equals("-tc")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				tcs.add(args[i]);
			} else if (arg.equals("-tp")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				tps.add(args[i]);
			} else if (arg.equals("-n")) {
				if (++i >= args.length)
					printUsage("Invalid arguments", 1);
				suiteName = args[i];
			} else {
				suiteFile = args[i];
				if (suiteName == null)
					suiteName = FilenameUtils.getBaseName(suiteFile);
				break;
			}
		}
		
		// Construct Runnable 
		ArrayList<Runnable> runnables = new ArrayList<Runnable>();
		try {
			for (String r : runnableClasses)
				runnables.add((Runnable) Class.forName(r).newInstance());
		} catch (Exception e) {
			log.error("Invalid runnable", e);
			printUsage("Runnable is invalid!", 1001);
		}
		
		// Register RunListeners
		JUnitCore core = new JUnitCore();
		core.addListener(new JUnitReporter());
		core.addListener(new JUnitFailureCollector());
		try {
			for (String l : listenerClasses)
				core.addListener((RunListener) Class.forName(l).newInstance());
		} catch (Exception e) {
			log.error("Invalid listener!", e);
			printUsage("Listener is invalid!", 1002);
		}
		
		// Add tests by various ways
		HashMap<String, HashSet<String>> testClasses = new HashMap<String, HashSet<String>>();
		
		if (suiteFile == null) {
			// by specifying test classes
			for (String s : tcs) 
				tc(s, testClasses);
			// by specifying test packages
			for (String s : tps)
				tp(s, testClasses);
		} else {
			try {
				List<String> lines = FileUtils.readLines(new File(suiteFile));
				for (String line : lines) {
					line = line.trim();
					if (line.startsWith("-tc")) {
						tc(line.substring(3), testClasses);
					} else if (line.startsWith("-tp")) {
						tp(line.substring(3), testClasses);
					} else if (!line.startsWith("#")) {
						tc(line, testClasses);
					}
				}
			} catch (IOException e) {
				log.error("Could not load the test suite file!", e);
				System.err.println("Could not load the test suite file!");
				System.exit(108);
			}
			
		}
		
		List<Runner> runners = new ArrayList<Runner>();
		for (Iterator<Entry<String, HashSet<String>>> i =  testClasses.entrySet().iterator(); i.hasNext();) {
			Entry<String, HashSet<String>> entry = i.next();
			final HashSet<String> methods = entry.getValue();
			final String klazz = entry.getKey();
			try {
				Runner runner = Request.aClass(Class.forName(klazz)).getRunner();
				
				if (entry.getValue() != ALL_METHODS) {
					new Filter() {
			            @Override
			            public boolean shouldRun(Description description) {
			                if (description.isTest()) 
			                    return methods.contains(description.getMethodName());
			                for (Description each : description.getChildren()) {
			                    if (shouldRun(each)) 
			                        return true;
			                }
			                return false;
			            }

			            @Override
			            public String describe() {
			                return String.format("Methods %s", methods);
			            }
			        }.apply(runner);
				}
				runners.add(runner);
			} catch (Exception e) {
				log.error(String.format("%s is not testable or has no test! Skip it!", klazz));
			}
		}
		
		// Start run all Runnable
		try {
			for (Runnable runnable : runnables) 
				runnable.run();
		} catch (Exception e) {
			log.error("Runnable failed!", e);
			System.err.println("Runnable failed!");
			System.exit(1004);
		}
		
		// Start run tests
		try {
			JUnitSuite suite = new JUnitSuite(suiteName, runners, thread);
			Result result = core.run(suite);
			if (!result.wasSuccessful())
				System.exit(1);
		} catch (InitializationError e) {
			//never occur
		}
	}
	

	 
	private static void tp(String arg, HashMap<String, HashSet<String>> classes) {
		arg = arg.trim();
		List<String> klasses = Utils.getClassesInPackage(arg);
		for (String k : klasses) {
			if (!k.matches(".+[$]{1}.*"))
				classes.put(k, ALL_METHODS);
		}
	}
	
	private static void tc(String arg, HashMap<String, HashSet<String>> classes) {
		arg = arg.trim();
		int i = arg.indexOf("-");
		if (i < 0) {
			classes.put(arg, ALL_METHODS);
		} else {
			String className = arg.substring(0, i++);
			String method = arg.substring(i);
			HashSet<String> methods = classes.get(className);
			if (methods == null) {
				methods = new HashSet<String>();
				classes.put(className, methods);
			}
				
			if (methods != ALL_METHODS)
				methods.add(method);
				
		}
	}
}
