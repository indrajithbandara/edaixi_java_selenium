package com.ibm.docs.test.gui.support;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EnhancedWebElements implements Iterable<EnhancedWebElement> {
	private String locator;
	
	private long timeout;
	
	protected By by;
	
	private EnhancedWebDriver driver;
	
	protected List<EnhancedWebElement> elements;
	
	private String description;
	
	public EnhancedWebElements(EnhancedWebDriver driver, String locator, long timeout, String description) {
		this.driver = driver;
		this.locator = locator;
		this.timeout = timeout;
		this.by = LocatorBuilder.build(locator);
		this.description = description;
	}
	
	public EnhancedWebElements setLocatorArgument(Object... args) {
		this.by = LocatorBuilder.build(locator, args);
		return this;
	}
	
	public By getLocator() {
		return by;
	}
	
	protected List<WebElement> findQuietly(long aTimeOut) {
		List<WebElement> elements = null;
		try {
			elements = aTimeOut <= 0 ? driver.findElements(by) : new WebDriverWait(driver, aTimeOut).until(presenceOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			//Log the exception
		}

		return elements;
	}
	
	public void waitPresence(long aTimeOut) {
		elements = null;
		List<WebElement> foundElements = findQuietly(aTimeOut);
		if (foundElements == null || foundElements.isEmpty())
			throw new NoSuchElementException(this.toString());
		elements = new ArrayList<EnhancedWebElement>(foundElements.size());
		for (WebElement e : foundElements) {
			elements.add(new EnhancedWebElement(driver, e));
		}
	}
	
	public void waitPresence() {
		waitPresence(timeout);
	}
	
	@Override
	public Iterator<EnhancedWebElement> iterator() {
		waitPresence();
		return elements.iterator();
	}
	
	public int size(){
		waitPresence();
		return elements.size();
	}
	
	public EnhancedWebElement get(int index){
		waitPresence();
		if (index < 0 || index >= elements.size())
			throw new NoSuchElementException(this.toString() + " Index: " + index);
		return elements.get(index);
	}
	
	public EnhancedWebElement getLast(){
		waitPresence();
		if (elements.isEmpty())
			throw new NoSuchElementException(this.toString() + " Index: last");
		return elements.get(elements.size() - 1);
	}
	
	public String[] getText(){
		waitPresence();
		String[] array = new String[elements.size()];
		for(int i=0;i<elements.size();i++){
			array[i] = elements.get(i).getText();
		}
		return array;
	}
	
	public String[] getAttribute(String name){
		waitPresence();
		String[] array = new String[elements.size()];
		for(int i=0;i<elements.size();i++){
			array[i] = elements.get(i).getAttribute(name);
		}
		return array;
	}
	
	public String[] getCssValue(String name) {
		waitPresence();
		String[] array = new String[elements.size()];
		for(int i=0;i<elements.size();i++){
			array[i] = elements.get(i).getCssValue(name);
		}
		return array;
	}
	

	public String toString() {
		return (description == null ? "" : description + " ") + (by == null ? "" : by);
	}
}
