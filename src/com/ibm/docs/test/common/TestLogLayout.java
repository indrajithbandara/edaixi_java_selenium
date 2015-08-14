package com.ibm.docs.test.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Test log layout.
 * @author liuzhe@cn.ibm.com
 *
 */
public class TestLogLayout extends HTMLLayout {
	private static final Pattern IMAGE_PATTERN = Pattern.compile("\\[Image:([^\\]]+)\\]", Pattern.CASE_INSENSITIVE);
	private static final String IMAGE_REPLACEMENT = "<img src=\"$1\"/>";
	private static final Pattern URL_PATTERN = Pattern.compile("((https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])", Pattern.CASE_INSENSITIVE);
	private static final String URL_REPLACEMENT = "<a href=\"$1\" target=\"_blank\">$1</a>";
	private static final String HEADER = Utils.getStringResource(TestLogLayout.class, "TestLogLayout.html");

	@Override
	public String getFooter() {
		// TODO Auto-generated method stub
		return super.getFooter();
	}

	@Override
	public String getHeader() {
		String ret = super.getHeader();
		return ret.replace("<table", HEADER + "<table");
	}

	@Override
	public String format(LoggingEvent event) {
		String text = super.format(event);
		Matcher matcher = URL_PATTERN.matcher(text);
		text = matcher.replaceAll(URL_REPLACEMENT);
		matcher = IMAGE_PATTERN.matcher(text);
		text = matcher.replaceAll(IMAGE_REPLACEMENT);
		return text;
	}
}
