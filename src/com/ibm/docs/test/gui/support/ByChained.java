package com.ibm.docs.test.gui.support;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByChained extends By {

	private By[] bys;

	public ByChained(By... bys) {
		this.bys = bys;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		List<WebElement> elements = findElements(context);
		if (elements.isEmpty())
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		return elements.get(0);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		if (bys.length == 0) {
			return new ArrayList<WebElement>();
		}

		List<WebElement> elems = null;
		for (By by : bys) {
			List<WebElement> newElems = new ArrayList<WebElement>();

			if (elems == null) {
				newElems.addAll(by.findElements(context));
			} else {
				for (WebElement elem : elems) {
					newElems.addAll(elem.findElements(by));
				}
			}
			elems = newElems;
		}

		return elems;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("By.chained: ");

		boolean first = true;
		for (By by : bys) {
			stringBuilder.append((first ? "{" : ", {")).append(by).append("}");
			first = false;
		}
		return stringBuilder.toString();
	}

}
