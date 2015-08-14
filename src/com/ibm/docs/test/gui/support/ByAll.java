package com.ibm.docs.test.gui.support;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ByAll extends By {

	private By[] bys;

	public ByAll(By... bys) {
		this.bys = bys;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		for (By by : bys) {
			try {
				return by.findElement(context);
			} catch (NoSuchElementException e) {
				// ignore
			}
		}
		throw new NoSuchElementException("Cannot locate an element using " + toString());
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		List<WebElement> elems = new ArrayList<WebElement>();
		for (By by : bys) {
			elems.addAll(by.findElements(context));
		}

		return elems;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("By.all: ");
		boolean first = true;
		for (By by : bys) {
			stringBuilder.append((first ? "{" : ", {")).append(by).append("}");
			first = false;
		}
		return stringBuilder.toString();
	}

}
