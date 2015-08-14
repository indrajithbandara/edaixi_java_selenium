package com.ibm.docs.test.gui.support;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Enhanced web element
 * @author liuzhe@cn.ibm.com
 *
 */
public class EnhancedWebElement implements WebElement, WrapsElement, Locatable {
	private static final Logger log = Logger.getLogger(Logger.class);
	protected String locator;

	private long timeout;
	
	protected By by;
	
	private EnhancedWebDriver driver;
	
	protected WebElement element;
	
	private String description;
	
	protected boolean cached = false;
	
	protected interface Action {
		void act();
	}
	
	public EnhancedWebElement(EnhancedWebDriver driver, String locator, long timeout, String description) {
		this.driver = driver;
		this.locator = locator;
		this.timeout = timeout;
		this.by = LocatorBuilder.build(locator);
		this.description = description;
	}
	
	public EnhancedWebElement(EnhancedWebDriver driver, WebElement element) {
		this.driver = driver;
		this.element = element;
		this.cached = true;
	}
	
	public EnhancedWebElement setLocatorArgument(Object... args) {
		if (locator == null)
			throw new IllegalArgumentException("Can't set locator arguments!");
		by = LocatorBuilder.build(locator, args);
		return this;
	}
	
	public By getLocator() {
		return by;
	}
	
	protected WebElement findQuietly(long aTimeOut) {
		if (cached)
			return element;
		
		WebElement element = null;
		try {
			element = aTimeOut <= 0 ? driver.findElement(by) : new WebDriverWait(driver, aTimeOut).until(presenceOfElementLocated(by));
		} catch (Exception e) {
			//Log the exception
		} 

		return element;
	}
	
	public void waitPresence(long aTimeOut) {
		element = findQuietly(aTimeOut);
		if (element == null)
			throw fillException(new NoSuchElementException("Element is not found!"));
		driver.sleep(500, TimeUnit.MILLISECONDS);
	}
	
	public void waitPresence() {
		waitPresence(timeout);
	}
	
	public boolean isPresent() {
		return findQuietly(0) != null;
	}
	
	protected WebDriverException fillException(WebDriverException e) {
		e.addInfo("Code info", description);
		e.addInfo("Locator info", by == null ? null : by.toString());
		return e;
	}
	
	public void act(Action action) {
		try {
			for (int i = 0 ; i < 2; i++) {
				waitPresence();
				try {
					action.act();
					break;
				} catch (ElementNotVisibleException e) {
					driver.sleep(2000, TimeUnit.MILLISECONDS);
					log.debug("Try agian", e);
				} catch (StaleElementReferenceException e) {
					driver.sleep(2000, TimeUnit.MILLISECONDS);
					log.debug("Try agian", e);
				} catch(InvalidElementStateException e) {
					driver.sleep(2000, TimeUnit.MILLISECONDS);
					log.debug("Try agian", e);
				}
			}
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
		//Improve stability
		driver.sleep(1000, TimeUnit.MILLISECONDS);
	}

	
	@Override
	public void click() {
		act(new Action() {
			@Override
			public void act() {
				element.click();
			}
			
		});
	}

	
	
	public void click(final int x, final int y) {
		act(new Action() {
			@Override
			public void act() {
				new Actions(driver.getWrappedDriver()).moveToElement(element, x, y).click().build().perform();
			}
			
		});
	}
	
	public void contextClick() {
		act(new Action() {
			@Override
			public void act() {
				new Actions(driver.getWrappedDriver()).contextClick(element).perform();
			}
		});
	}
	
	public void doubleClick() {
		act(new Action() {
			@Override
			public void act() {
				new Actions(driver.getWrappedDriver()).doubleClick(element).build().perform();
			}
		});
	}
	
	public void doubleClick(final int x, final int y) {
		act(new Action() {
			@Override
			public void act() {
				new Actions(driver.getWrappedDriver()).moveToElement(element, x, y).doubleClick().build().perform();
			}
		});
	}
	
	@Override
	public void submit() {
		act(new Action() {
			@Override
			public void act() {
				element.submit();
			}
		});
	}

	@Override
	public void sendKeys(final CharSequence... keysToSend) {
		act(new Action() {
			@Override
			public void act() {
				element.sendKeys(keysToSend);
			}
		});
	}

//	public void sendDelayedKeys(CharSequence... keysToSend) {
//		waitPresence();
//		try {
//			for (CharSequence c : keysToSend) {
//				element.sendKeys(c);
//				driver.sleep(500, TimeUnit.MILLISECONDS);
//			}
//		} catch (WebDriverException e) {
//			handleException(e);
//			throw e;
//		}
//		//Improve stability
//		driver.sleep(1000, TimeUnit.MILLISECONDS);
//	}
	
	
	@Override
	public void clear() {
		act(new Action() {
			@Override
			public void act() {
				element.clear();
			}
		});
	}

	@Override
	public String getTagName() {
		waitPresence();
		try {
			return element.getTagName();
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}

	@Override
	public String getAttribute(String name) {
		waitPresence();
		try {
			return element.getAttribute(name);
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}

	@Override
	public boolean isSelected() {
		waitPresence();
		try {
			return element.isSelected();
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}

	@Override
	public boolean isEnabled() {
		waitPresence();
		try {
			return element.isEnabled();
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}

	@Override
	public String getText() {
		waitPresence();
		try {
			return element.getText();
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}

	@Override
	public List<WebElement> findElements(By by) {
		waitPresence();
		return element.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		waitPresence();
		return element.findElement(by);
	}

	@Override
	public boolean isDisplayed() {
		waitPresence();
		return element.isDisplayed();
	}

	@Override
	public Point getLocation() {
		waitPresence();
		return element.getLocation();
	}

	@Override
	public Dimension getSize() {
		waitPresence();
		return element.getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		waitPresence();
		try {
			return element.getCssValue(propertyName);
		} catch (WebDriverException e) {
			fillException(e);
			throw e;
		}
	}
	
	public String getListStyle(){
		String value = "";				
		String js = "var v='';"	
			   +"var e=arguments[0];"			   
			   +"if(window.getComputedStyle)"
			   +"{ v=window.getComputedStyle(e," + "\":before\")[\"content\"]; }"
			   +"else if(e.currentStyle){ v=e.style.content; }"
			   +"else v='not support style';"
			   +"return v;";
		
			
		value = (String)driver.javascript(js, this);	
		return value;
	}
	
	//TODO: do not work for "wordWrap" property
	public String getComputedCssValue(String propertyName){
		waitPresence();
		String value = "";	
		
		String js = "var v='';"	
			   +"var e=arguments[0];"			   
			   +"if(window.getComputedStyle)"
			   +"{ v=window.getComputedStyle(e,false)[\"" + propertyName + "\"]; }"			   
			   +"else if(e.currentStyle){ v=e.currentStyle[\""+propertyName + "\"]; }"
			   +"else v='not support style';"
			   +"return v;";
		
		value = (String)driver.javascript(js, this);		
		
		return value;
	}
	

	public String toString() {
		return (description == null ? "" : description + " ") + (by == null ? "" : by);
	}

	@Override
	public WebElement getWrappedElement() {
		waitPresence();
		return element;
	}

	@Override
	public Coordinates getCoordinates() {
		waitPresence();
		return ((Locatable) element).getCoordinates();
	}
	
	/**
	 * Highlight a web element. Useful for debug
	 */
	public void highlight() {
		waitPresence();
		driver.javascript("var element = arguments[0],original=element.style.backgroundColor,id = setInterval(function(){element.style.backgroundColor = element.style.backgroundColor == original ? 'red' : original;}, 200);setTimeout(function(){clearInterval(id);element.style.backgroundColor=original;},2000);", element);
	}
	
	/**
	 * Get inner html of the element. Useful for debug
	 * @return
	 */
	public String getInnerHTML() {
		waitPresence();
		return (String )driver.javascript("return arguments[0].innerHTML;", element);
	}
	
	/**
	 * Get outer html of the element. Useful for debug
	 * @return
	 */
	public String getOuterHTML() {
		waitPresence();
		return (String )driver.javascript("return arguments[0].outerHTML || new XMLSerializer().serializeToString(arguments[0]);", element);
	}
	
}
