package com.sc.dns;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * SymbolMatcher represents a map which ahs mapping in between digital
 * representation and actual number.
 * 
 * @author ajay
 *
 */
public class SymbolMatcher {
	//private static final Logger log = Logger.getLogger(SymbolMatcher.class);
	private static final Map<String, Integer> inputCharToDigitMap = new HashMap<String, Integer>();
	static {
		String ch0 = " _ | ||_|";
		inputCharToDigitMap.put(ch0, 0);
		String ch1 = "     |  |";
		inputCharToDigitMap.put(ch1, 1);
		String ch2 = " _  _||_ ";
		inputCharToDigitMap.put(ch2, 2);
		String ch3 = " _  _| _|";
		inputCharToDigitMap.put(ch3, 3);
		String ch4 = "   |_|  |";
		inputCharToDigitMap.put(ch4, 4);
		String ch5 = " _ |_  _|";
		inputCharToDigitMap.put(ch5, 5);
		String ch6 = " _ |_ |_|";
		inputCharToDigitMap.put(ch6, 6);
		String ch7 = " _   |  |";
		inputCharToDigitMap.put(ch7, 7);
		String ch8 = " _ |_||_|";
		inputCharToDigitMap.put(ch8, 8);
		String ch9 = " _ |_| _|";
		inputCharToDigitMap.put(ch9, 9);
	}

	public static String matchSymbol(String s) {
		if (inputCharToDigitMap.containsKey(s)) {
			// log.info("Match Found");
			return String.valueOf(inputCharToDigitMap.get(s));
		} else {
			// log.warn("No Match Found");
			return "?";
		}

	}

}
