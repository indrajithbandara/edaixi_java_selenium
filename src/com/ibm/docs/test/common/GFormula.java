package com.ibm.docs.test.common;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Instance of FormulaTranslate.js
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class GFormula {
	private static GFormula instance;
	private JSONObject jsonObject;

	private GFormula() {
		String json = Utils.getStringResource(GFormula.class,
				"FormulaTranslate.json");
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the instance of FormulaTranslate.json
	 */
	public static GFormula getInstance() {
		if (instance == null) {
			instance = new GFormula();
		}
		return instance;
	}

	/**
	 * Translate given formula to given locale;
	 */
	public String translate(String formula, String localeID) {
		String lan = new ULocale(localeID).getLanguage();
		try {
			JSONObject jsonMap = jsonObject.getJSONObject("en2" + lan);
			return jsonMap.getString(formula);
		} catch (JSONException e) {
			// e.printStackTrace();
			return formula;
		}
	}

	/**
	 * get formula argument separator in given locale;
	 */
	public static char getSeparator(String localeID) {
		DecimalFormatSymbols dfs = DecimalFormatSymbols
				.getInstance(new ULocale(localeID));
		return dfs.getDecimalSeparator() == ',' ? ';' : ',';
	}

	/**
	 * parse the formula.
	 */
	public static String parse(String text, String localeID) {
		return parse(text, localeID, false);
	}

	/**
	 * parse the formula.
	 */
	public static String parse(String text, String localeID, boolean formatDate) {
		StringTokenizer tokenizer = new StringTokenizer(text, "=()+-*/,'\"",
				true);
		int tl = tokenizer.countTokens();
		String[] tokens = new String[tl];
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			tokens[i] = token;
			i++;
		}
		scanRef(tokens);
		scanDQuotes(tokens);
		scanBool(tokens, localeID);
		return scanBrackects(tokens, localeID, formatDate);
	}

	private static void scanRef(String[] tokens) {
		Stack<Integer> stack = new Stack<Integer>();
		int length = tokens.length;
		for (int i = 0; i < length; i++) {
			if (tokens[i].equals("'")) {
				if (stack.empty())
					stack.push(i);
				else {
					if (tokens[i + 1].charAt(0) == '!') {
						int s = stack.pop();
						for (int j = s + 1; j <= i; j++) {
							tokens[s] += tokens[j];
							tokens[j] = "";
						}
					} else if (tokens[i + 1].equals("'")) {
						i++;
					}
				}
			}
		}
	}

	private static void scanDQuotes(String[] tokens) {
		Stack<Integer> stack = new Stack<Integer>();
		int length = tokens.length;
		for (int i = 0; i < length; i++) {
			if (tokens[i].equals("\"")) {
				if (stack.empty())
					stack.push(i);
				else {
					int s = stack.pop();
					for (int j = s + 1; j <= i; j++) {
						tokens[s] += tokens[j];
						tokens[j] = "";
					}
				}
			}
		}

	}

	private static void scanBool(String[] tokens, String localeID) {
		int length = tokens.length;
		for (int i = 0; i < length; i++) {
			if (tokens[i].equalsIgnoreCase("true"))
				tokens[i] = getInstance().translate(tokens[i].toUpperCase(),
						localeID);
			else if (tokens[i].equalsIgnoreCase("false"))
				tokens[i] = getInstance().translate(tokens[i].toUpperCase(),
						localeID);
		}
	}

	private static String scanBrackects(String[] tokens, String localeID,
			boolean formatDate) {
		String[] dateF = { "DATEDIF", "DAYS", "DAYS360", "NETWORKDAYS",
				"WORKDAY" };
		HashSet<String> dateFS = new HashSet<String>();
		for (String s : dateF)
			dateFS.add(s);
		LinkedList<Integer> fins = new LinkedList<Integer>();
		String word = "";
		Stack<Integer> stack = new Stack<Integer>();
		String current = "";
		int length = tokens.length;
		for (int i = 0; i < length; i++) {
			if (tokens[i].equals("("))
				stack.push(i);
			else if (tokens[i].equals(")") && !stack.empty()) {
				int s = stack.pop();
				if (Character.isLetter(tokens[s - 1].charAt(0))) {
					current = tokens[s - 1].toUpperCase();
					if (!fins.contains(s - 1)) {
						tokens[s - 1] = GFormula.getInstance().translate(
								tokens[s - 1].toUpperCase(), localeID);
						fins.add(s - 1);
					}
				}
				for (int j = s + 1; j < i; j++) {
					if (!fins.contains(j)) {
						if (dateFS.contains(current) & (j + 4 < i)) {
							if (tokens[j + 1].equals("/")
									& tokens[j + 3].equals("/")
									& (tokens[j].length() != 0)
									& (tokens[j + 2].length() != 0)
									& (tokens[j + 4].length() != 0)
									& !fins.contains(j + 1)
									& !fins.contains(j + 2)
									& !fins.contains(j + 3)
									& !fins.contains(j + 4)) {
								if (Character.isDigit((tokens[j].charAt(0)))
										& Character.isDigit((tokens[j + 2]
												.charAt(0)))
										& Character.isDigit((tokens[j + 4]
												.charAt(0)))) {
									tokens[j] = tokens[j] + tokens[j + 1]
											+ tokens[j + 2] + tokens[j + 3]
											+ tokens[j + 4];
									if (formatDate) {
										tokens[j] = parseDate(tokens[j],
												localeID);
									}
									tokens[j + 1] = "";
									tokens[j + 2] = "";
									tokens[j + 3] = "";
									tokens[j + 4] = "";
									fins.add(j);
									fins.add(j + 1);
									fins.add(j + 2);
									fins.add(j + 3);
									fins.add(j + 4);
									j = j + 4;
								}
							}
						} else {
							tokens[j] = parseString(tokens[j], localeID);
							fins.add(j);
						}
					}
				}
				fins.add(s);
				fins.add(i);
				current = "";
			}
		}
		for (int i = 0; i < length; i++) {
			if (!fins.contains(i))
				tokens[i] = parseString(tokens[i], localeID);
			word += tokens[i];
		}
		return word;
	}

	private static String parseString(String text, String localeID) {
		String word = "";
		if (text.length() == 0)
			;
		else if (text.equals(",")) {
			word += GFormula.getSeparator(localeID);
			return word;
		} else if (Character.isDigit(text.charAt(0))) {
			Double b = new Double(text);
			if (!b.isNaN()) {
				ULocale locale = new ULocale(localeID);
				NumberFormat numberFt = NumberFormat.getNumberInstance(locale);
				numberFt.setMaximumFractionDigits(15);
				numberFt.setGroupingUsed(false);
				word += numberFt.format(b);
				return word;
			}
		}
		return text;
	}

	private static String parseDate(String text, String localeID) {
		String dateString = null;
		ULocale locale = new ULocale(localeID);
		DateFormat dateFormat = DateFormat.getDateInstance(2, locale);
		try {
			Date date = DateFormat.getDateInstance(3, new ULocale("en-US"))
					.parse(text);
			dateString = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateString;
	}
}
