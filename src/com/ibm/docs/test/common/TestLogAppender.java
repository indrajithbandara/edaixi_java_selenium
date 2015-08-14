package com.ibm.docs.test.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * It's a log4j appender, used to generate logs into file per thread
 * 
 * @author liuzhe@cn.ibm.com
 *
 */
public class TestLogAppender extends AppenderSkeleton {
	
	private final ConcurrentHashMap<Long, BufferedWriter> writers = new ConcurrentHashMap<Long, BufferedWriter>();

	public TestLogAppender() {

	}

	/**
	 * Attach the current thread to the file. After calling this method, all logs in the current thread will
	 * be stored in the given file.
	 * 
	 * @param file
	 */
	public void attach(File file) {
		detach();
		try {
			FileUtils.forceMkdir(file.getParentFile());
			writers.put(Thread.currentThread().getId(), new BufferedWriter(new FileWriter(file)));
			writeHeader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Detach the current thread. Logs in the current thread will not be logged.
	 * 
	 */
	public void detach() {
		writeFooter();
		long tid = Thread.currentThread().getId();
		BufferedWriter bw = writers.get(tid);
		if (bw != null) {
			IOUtils.closeQuietly(bw);
			writers.remove(tid);
		}
	}
	
	private BufferedWriter getWriter() {
		return writers.get(Thread.currentThread().getId());
	}
	
	@Override
	public void append(LoggingEvent event) {
		try {
			BufferedWriter bw = getWriter();
			if (bw != null) {
				bw.write(this.layout.format(event));
				if (layout.ignoresThrowable()) {
					String[] s = event.getThrowableStrRep();
					if (s != null) {
						int len = s.length;
						for (int i = 0; i < len; i++) {
							bw.write(s[i]);
							bw.write(Layout.LINE_SEP);
						}
					}
				}
				
				bw.flush();
			}
		} catch (Exception e) {
			//ignored
		}
	}

	
	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void close() {
		for (Iterator<BufferedWriter> i = writers.values().iterator(); i.hasNext();) 
			IOUtils.closeQuietly(i.next());
		writers.clear();
	}


	/**
	 * Write a footer as produced by the embedded layout's
	 * {@link Layout#getFooter} method.
	 */
	protected void writeFooter() {
		if (layout == null)
			return;
		String f = layout.getFooter();
		if (f == null)
			return;
		BufferedWriter bw = getWriter();
		if (bw == null)
			return;
		
		try {
			bw.write(f);
			bw.flush();
		} catch (Exception e) {

		}
	}

	/**
	 * Write a header as produced by the embedded layout's
	 * {@link Layout#getHeader} method.
	 */
	protected void writeHeader() {
		if (layout == null)
			return;
		String f = layout.getHeader();
		if (f == null)
			return;
		BufferedWriter bw = getWriter();
		if (bw == null)
			return;
		
		try {
			bw.write(f);
			bw.flush();
		} catch (Exception e) {

		}
	}
}