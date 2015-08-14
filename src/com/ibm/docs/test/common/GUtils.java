package com.ibm.docs.test.common;

import java.text.ParseException;
import java.util.Date;

import com.ibm.docs.test.gui.support.EnhancedWebDriver;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.util.ULocale;

/**
 * GVT utilities
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class GUtils {

	/**
	 * @return browser preference language
	 */
	public static String getBrowserPreferenceLan(EnhancedWebDriver driver) {
		String language;
		if (driver.isInternetExplorer()) {
			language = (String) driver
					.javascript("return navigator.userLanguage");
			return language;
		} else {
			language = (String) driver.javascript("return navigator.language");
			return language;
		}
	}

	/**
	 * Translate a text into its Unicode.
	 */
	public static String getHexUnicode(String text) {
		String result;
		Transliterator transliterator = Transliterator
				.getInstance("Any-Hex/Unicode");
		result = transliterator.transform(text);
		return result;
	}

	/**
	 * Get a String form its Unicode Point Hex.
	 */
	public static String getStringByHex(String hex) {
		Transliterator transliterator = Transliterator
				.getInstance("Hex-Any");
		return transliterator.transform(hex);
	}

	/**
	 * Trim the no-breaking space nbsp/00a0.
	 */
	public static String trimNBSP(String src) {
		int b = 0;
		int e = src.length() - 1;
		char[] chars = src.toCharArray();
		for (; b < e; b++) {
			if (chars[b] != '\u00a0')
				break;
		}
		for (; e > b; e--) {
			if (chars[e] != '\u00a0')
				break;
		}
		return src.substring(b, e + 1);
	}

	/**
	 * @param style
	 *            0 is FULL style, 1 is LONG style, 2 is MEDIUM style, 3 is
	 *            SHORT style.
	 * @return formated local date.
	 */
	public static String getLocalDate(int style, String localeID) {
		String dateString;
		ULocale locale = new ULocale(localeID);
		DateFormat dateFormat = DateFormat.getDateInstance(style, locale);
		dateString = dateFormat.format(new Date());
		return Normalizer.normalize(dateString, Normalizer.NFKC);
	}

	/**
	 * @param style
	 *            0 is FULL style, 1 is LONG style, 2 is MEDIUM style, 3 is
	 *            SHORT style.
	 * @return formated local time.
	 */
	public static String getLocalTime(int style, String localeID) {
		String timeString;
		ULocale locale = new ULocale(localeID);
		DateFormat timeFormat = DateFormat.getTimeInstance(style, locale);
		timeString = timeFormat.format(new Date());
		return Normalizer.normalize(timeString, Normalizer.NFKC);
	}

	/**
	 * @return SHORT style local date and time.
	 */
	public static String getLocalDateAndTime(String localeID) {
		String dateAndTimeString;
		ULocale locale = new ULocale(localeID);
		DateFormat dateAndTimeFormat = DateFormat.getDateTimeInstance(
				DateFormat.SHORT, DateFormat.SHORT, locale);
		dateAndTimeString = dateAndTimeFormat.format(new Date());
		return Normalizer.normalize(dateAndTimeString, Normalizer.NFKC);
	}

	/**
	 * @param dateStrOri
	 *            a Date string in en-US short.
	 * @param style
	 *            0 is FULL style, 1 is LONG style, 2 is MEDIUM style, 3 is
	 *            SHORT style.
	 * @return formated date.
	 */
	public static String getFormattedDate(String dateStrOri, int style,
			String localeID) {
		String dateString = null;
		ULocale locale = new ULocale(localeID);
		DateFormat dateFormat = DateFormat.getDateInstance(style, locale);
		try {
			Date date = DateFormat.getDateInstance(3, new ULocale("en-US"))
					.parse(dateStrOri);
			dateString = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Normalizer.normalize(dateString, Normalizer.NFKC);
	}

	/**
	 * @param timeStrOri
	 *            a Time string in en-US short.
	 * @param style
	 *            0 is FULL style, 1 is LONG style, 2 is MEDIUM style, 3 is
	 *            SHORT style.
	 * @return formated time.
	 */
	public static String getFormattedTime(String timeStrOri, int style,
			String localeID) {
		String timeString = null;
		ULocale locale = new ULocale(localeID);
		DateFormat dateFormat = DateFormat.getTimeInstance(style, locale);
		try {
			Date date = DateFormat.getTimeInstance(3, new ULocale("en-US"))
					.parse(timeStrOri);
			timeString = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Normalizer.normalize(timeString, Normalizer.NFKC);
	}

	/**
	 * @param dateTimeStrOri
	 *            a Date and Time string in en-US short.
	 * @param style
	 *            0 is FULL style, 1 is LONG style, 2 is MEDIUM style, 3 is
	 *            SHORT style.
	 * @return formated date and time.
	 */
	public static String getFormattedDateAndTime(String dateTimeStrOri,
			int style, String localeID) {
		String dateTimeString = null;
		ULocale locale = new ULocale(localeID);
		DateFormat dateFormat = DateFormat.getDateTimeInstance(style, style,
				locale);
		try {
			Date date = DateFormat.getDateTimeInstance(3, 3,
					new ULocale("en-US")).parse(dateTimeStrOri);
			dateTimeString = dateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Normalizer.normalize(dateTimeString, Normalizer.NFKC);
	}

	/**
	 * Format a double number to a string with specified locale and fraction
	 * digits number.
	 * 
	 * @param fractionCnt
	 *            Max and min fraction digits number.
	 * @param grouping
	 *            Sets whether or not grouping will be used in this format.
	 */
	public static String getFormattedNumber(double number, String localeID,
			int fractionNum, boolean grouping) {
		ULocale locale = new ULocale(localeID);
		NumberFormat numberFt = NumberFormat.getNumberInstance(locale);
		numberFt.setMaximumFractionDigits(fractionNum);
		numberFt.setMinimumFractionDigits(fractionNum);
		if (!grouping)
			numberFt.setGroupingUsed(grouping);
		return Normalizer.normalize(numberFt.format(number), Normalizer.NFKC);
	}

	/**
	 * Format a double number to a currency string with specified locale and
	 * fraction digits number.
	 * 
	 * @param fractionCnt
	 *            Max and min fraction digits number, if set to -1 then will
	 *            ignore this field.
	 */
	public static String getFormattedCurrency(double number, String localeID,
			int fractionNum) {
		ULocale locale = new ULocale(localeID);
		NumberFormat currencyFt = NumberFormat.getCurrencyInstance(locale);
		if (fractionNum != -1) {
			currencyFt.setMaximumFractionDigits(fractionNum);
			currencyFt.setMinimumFractionDigits(fractionNum);
		}
		return Normalizer.normalize(currencyFt.format(number), Normalizer.NFKC);
	}

	/**
	 * Format a double number to a percent string with specified locale and
	 * fraction digits number.
	 * 
	 * @param fractionCnt
	 *            Msax and min fraction digits number.
	 */
	public static String getFormattedPercent(double number, String localeID,
			int fractionNum, boolean grouping) {
		ULocale locale = new ULocale(localeID);
		NumberFormat percentFt = NumberFormat.getPercentInstance(locale);
		percentFt.setMaximumFractionDigits(fractionNum);
		percentFt.setMinimumFractionDigits(fractionNum);
		if (!grouping)
			percentFt.setGroupingUsed(grouping);
		return Normalizer.normalize(percentFt.format(number), Normalizer.NFKC);
	}

	public static String getCurrencySymbol(String localeID) {
		ULocale locale = new ULocale(localeID);
		return DecimalFormatSymbols.getInstance(locale).getCurrencySymbol();
	}
}
