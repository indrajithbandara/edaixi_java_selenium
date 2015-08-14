package com.ibm.docs.test.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Generate report for JUnit, including one XML and one HTML report.
 * @author liuzhe@cn.ibm.com
 */
public class JUnitReporter extends RunListener {

	private File outputDir = new File(Config.getInstance().getTestspace());
	
	private Document document = null;
	
	private Element testsuiteElement = null;
	
	private HashMap<Description, Element> testcaseElements = new  HashMap<Description, Element>();
	
	private int failures = 0;
	
	private int errors = 0;
	
	private int tests = 0;
	
	private int ignored = 0;
	
	private int skipped = 0;
	
	private int started = 0;
	
	private int finished = 0;
	
	@Override
	public void testStarted(Description description) throws Exception {
		started++;
		Element testcaseElement = testcaseElements.get(description);
		testcaseElement.setAttribute("start",  Long.toString(System.currentTimeMillis()));
		store();
	}
	
	@Override
	public void testAssumptionFailure(Failure failure) {
		skipped++;
		Element testcaseElement = testcaseElements.get(failure.getDescription());
		Element failureEl = document.createElement("skipped");
		failureEl.setAttribute("message", failure.getMessage());
		failureEl.setAttribute("type", failure.getTestHeader());
		failureEl.setTextContent(failure.getTrace());
		testcaseElement.appendChild(failureEl);
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		ignored++;
		testStarted(description);
		Ignore ignore = description.getAnnotation(Ignore.class);
		Element ignoredEl = document.createElement("ignored");
		ignoredEl.setAttribute("message", ignore.value());
		Element testcaseElement = testcaseElements.get(description);
		testcaseElement.appendChild(ignoredEl);
		testFinished(description);
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		Element testcaseElement = testcaseElements.get(failure.getDescription());
		if (failure.getException() instanceof AssertionError) {
			failures++;
			Element failureEl = document.createElement("failure");
			failureEl.setAttribute("message", failure.getMessage());
			failureEl.setAttribute("type", failure.getTestHeader());
			failureEl.setTextContent(failure.getTrace());
			testcaseElement.appendChild(failureEl);
		} else {
			errors++;
			Element errorEl = document.createElement("error");
			errorEl.setAttribute("message", failure.getMessage());
			errorEl.setAttribute("type", failure.getTestHeader());
			errorEl.setTextContent(failure.getTrace());
			testcaseElement.appendChild(errorEl);
		}
	}

	@Override
	public void testFinished(Description description) throws Exception {
		finished++;
		Element testcaseElement = testcaseElements.get(description);
		HashMap<String, String> props = JUnitExtra.getExtra(description);
		if (props != null) {
			for (Iterator<Entry<String, String>> i = props.entrySet().iterator();i.hasNext();) {
				Element propertyEl = document.createElement("property");
				Entry<String, String> e = i.next();
				propertyEl.setAttribute("name", e.getKey());
				propertyEl.setAttribute("value", e.getValue());
				testcaseElement.appendChild(propertyEl);
			}
		}
		testcaseElement.setAttribute("end", Long.toString(System.currentTimeMillis()));
		store();
	}


	@Override
	public void testRunStarted(Description description) throws Exception {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		testsuiteElement = document.createElement("testsuite");
		document.appendChild(testsuiteElement);
		mkTestcaseElement(description);
		testsuiteElement.setAttribute("name", description.getDisplayName());
		testsuiteElement.setAttribute("start", Long.toString(System.currentTimeMillis()));
		store();
	}
	
	private void mkTestcaseElement(Description description) {
		if (description.isTest()) {
			tests++;
			Element element = document.createElement("testcase");
			element.setAttribute("classname", description.getClassName());
			element.setAttribute("methodname", description.getMethodName());
			element.setAttribute("id", Utils.uuid());
			testsuiteElement.appendChild(element);
			TestDoc doc = description.getAnnotation(TestDoc.class);
			if (doc != null)
				element.setAttribute("doc", doc.value());
			TestLink link = description.getAnnotation(TestLink.class);
			if (link != null)
				element.setAttribute("link", link.value());
			testcaseElements.put(description, element);
		}

		for (Description c : description.getChildren()) 
			mkTestcaseElement(c);
	}

	
	@Override
	public void testRunFinished(Result result) throws Exception {
		store();
	}

	private void store() {
		if (document == null)
			return;	
		testsuiteElement.setAttribute("tests", Long.toString(tests));
		testsuiteElement.setAttribute("finished", Long.toString(finished));
		testsuiteElement.setAttribute("running", Long.toString(started - finished));
		testsuiteElement.setAttribute("failures", Long.toString(failures));
		testsuiteElement.setAttribute("errors", Long.toString(errors));
		testsuiteElement.setAttribute("ignored", Long.toString(ignored));
		testsuiteElement.setAttribute("skipped", Long.toString(skipped));
		testsuiteElement.setAttribute("passed", Long.toString(finished - failures - errors - ignored - skipped));
		testsuiteElement.setAttribute("duration", Long.toString(System.currentTimeMillis() - Long.parseLong(testsuiteElement.getAttribute("start"))));
		NodeList nodes = testsuiteElement.getChildNodes();
		for (int i = nodes.getLength() - 1; i >= 0; i--){
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element e = (Element) node;
				if ("property".equals(e.getTagName())) {
					testsuiteElement.removeChild(e);
				}
			}
		}
		
		for (Iterator<Entry<String, String>> i = JUnitExtra.getExtra().entrySet().iterator();i.hasNext();) {
			Element propertyEl = document.createElement("property");
			Entry<String, String> e = i.next();
			propertyEl.setAttribute("name", e.getKey());
			propertyEl.setAttribute("value", e.getValue());
			testsuiteElement.appendChild(propertyEl);
		}
		
		
		try {
			File xmlReportFile = new File(outputDir, "report.xml");
			if (xmlReportFile.getParentFile() != null)
				xmlReportFile.getParentFile().mkdirs();
			File htmlReportFile = new File(outputDir, "report.html");
			StreamSource xsl = new StreamSource(getClass().getResourceAsStream(getClass().getSimpleName() + ".xsl"));
			StreamResult xmlResult = new StreamResult(new FileOutputStream(xmlReportFile));
			StreamResult htmlResult = new StreamResult(new FileOutputStream(htmlReportFile));
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.newTransformer().transform(new DOMSource(document), xmlResult);
			transformerFactory.newTransformer(xsl).transform(new DOMSource(document), htmlResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
