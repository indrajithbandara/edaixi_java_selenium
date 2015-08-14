package com.ibm.docs.test.gui.support;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.openqa.selenium.By;

public class LocatorBuilder {
	private static final int LOCATOR_EOF = 1;
	private static final int ESCAPE = 2;
	private static final int LOCATOR_BRACE = 3;
	private static final int INIT = 4;

	/**
	 * Lexical analyzer
	 * @param text
	 * @param args
	 * @return
	 */
	private static LinkedList<Object> scan(String text, Object... args) {
		StringTokenizer tokenizer = new StringTokenizer(text, "{}()+-$0123456789", true);
		int state = INIT, lastState = INIT;
		String word = "";
		LinkedList<Object> words = new LinkedList<Object>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			char c = token.charAt(0);

			switch (state) {
			case INIT:
				if (c == '+' || c == '-' || c == '(' || c == ')') {
					words.add(c);
				} else if (c == '{') {
					state = LOCATOR_BRACE;
				} else if (c == '$'){
					lastState = LOCATOR_EOF;
					state = ESCAPE;
				} else {
					word += token;
					state = LOCATOR_EOF;
				}
				break;
			case LOCATOR_EOF:
				if (c == '$') {
					lastState = LOCATOR_EOF;
					state = ESCAPE;
				} else {
					word += token;
				}
				break;
			case LOCATOR_BRACE:
				if (c == '$') {
					lastState = LOCATOR_BRACE;
					state = ESCAPE;
				} else if (token.equals("}")) {
					words.add(word);
					state = INIT;
					word = "";
				} else {
					word += token;
				}
				break;
			case ESCAPE:
				if (c == '$' || c == '{' || c == '}')
					word += c;
				else if (c >= '0' && c <= '9' && (c -= '0') < args.length)
					word += args[c];
				else
					word += "$" + token;

				state = lastState;
				break;

			}
		}

		if (state == LOCATOR_EOF) {
			words.add(word);
			state = INIT;
		}
		
		if (state != INIT) 
			throw new IllegalArgumentException(String.format("Invalid locator expression: %s. Expected '}'", text));
		return words;
	}
	
	
	public static By build(String text, Object... args) {
		LinkedList<Object> words = scan(text, args);
		if (words.isEmpty())
			throw new IllegalArgumentException("Locator expression is empty!");
		//System.out.println(words);
		LinkedList<Character> stack = new LinkedList<Character>();
		LinkedList<Object> postfix = new LinkedList<Object>();
		for (Object w : words) {
			if (w instanceof String) {
				postfix.add(w);
			} else {
				// operator
				char c = (Character) w;
				if (c == '(') {
					stack.push(c);
				} else if (c == ')') {
					boolean leftFound = false;
					while (!stack.isEmpty()) {
						char top = stack.pop();
						if (top != '(')
							postfix.add(top);
						else {
							leftFound = true;
							break;
						}
					}
					
					if (!leftFound)
						throw new IllegalArgumentException(String.format("Invalid locator expression: %s. Expected '('", text));
					
				} else {
					if (stack.isEmpty() || stack.getFirst() == '(')
						stack.push(c);
					else {
						postfix.add(stack.pop());
						stack.push(c);
					}
				}
				
				
			}
			
		}
		while (!stack.isEmpty()) {
			char top = stack.pop();
			if (top == '(')
				throw new IllegalArgumentException(String.format("Invalid locator expression: %s. Expected ')'", text));
			
			postfix.add(top);
		}
		
		//System.out.println(postfix);
		LinkedList<By> bys = new LinkedList<By>();

		for (Object o : postfix) {
			if (o instanceof String) {
				bys.push(mkBy((String)o));
			} else {
				
				try {
					char c = (Character) o;
					By by1 = bys.pop();
					By by2 = bys.pop();
					if (c == '+') {
						bys.push(new ByAll(new By[]{by2, by1}));
					} else if (c == '-') {
						bys.push(new ByChained(new By[]{by2, by1}));
					}
				} catch (NoSuchElementException e) {
					throw new IllegalArgumentException(String.format("Invalid locator expression: %s. Unexpected '+' or '-'", text));
				}
			}
		}
		if (bys.size() != 1)
			throw new IllegalArgumentException(String.format("Invalid locator expression: %s. Missed '+' or '-'", text));

		return bys.getFirst();
	}
	
	public static void main(String...args) {
		build("{1}+{2}+{3}}");

	}
	
	protected static By mkBy(String locator) {
		if (locator.startsWith("css="))
			return By.cssSelector(locator.substring("css=".length()));

		if (locator.startsWith("id="))
			return By.id(locator.substring("id=".length()));

		if (locator.startsWith("tag="))
			return By.tagName(locator.substring("tag=".length()));

		if (locator.startsWith("xpath="))
			return By.xpath(locator.substring("xpath=".length()));
		
		if (locator.startsWith("link="))
			return By.linkText(locator.substring("link=".length()));
		
		if (locator.startsWith("partialLink="))
			return By.partialLinkText(locator.substring("partialLink=".length()));
		
		if (locator.startsWith("name="))
			return By.name(locator.substring("name=".length()));
		
		if (locator.startsWith("className="))
			return By.className(locator.substring("className=".length()));
		
		if (locator.startsWith("script="))
			return new ByScript(locator.substring("script=".length()));

		return By.cssSelector(locator);
	}
	
}
