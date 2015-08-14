package com.ibm.docs.test.gui.support;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByScript extends By {
	private String code;

	public ByScript(String code) {
		super();
		this.code = code;
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (context instanceof JavascriptExecutor) {
			List<WebElement> elements = new ArrayList<WebElement>();
			JavascriptExecutor executor = (JavascriptExecutor) context;
			Object e = executor.executeScript(code);
			if (e instanceof WebElement) {
				elements.add((WebElement) e);
			} else if (e instanceof List) {
				List<?> es = (List<?>) e;
				for (Object obj : es) {
					if (obj instanceof WebElement)
						elements.add((WebElement) obj);
				}
			}

			return elements;
		}
		return null;
	}

	@Override
	public String toString() {
		 return "By.selector: " + code;
	}

}
