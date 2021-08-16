package com.sc.dns;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * Input Processor is responsible to get the Digital Number from Queue and then
 * process that and find the actual number. If the input Digital number is not
 * matching then replace it with "?"
 * 
 * @author ajay
 *
 */

public class InputProcessor implements Runnable {
	private static final Logger log = Logger.getLogger(InputProcessor.class);

	public InputProcessor(BlockingQueue<DigitalNumber> numQueue, CountDownLatch latch) {
		super();
		this.numQueue = numQueue;
		this.latch = latch;
	}

	private final BlockingQueue<DigitalNumber> numQueue;
	private final CountDownLatch latch;
	private final String ILL = "ILL";
	private static final int LEGAL_LENGTH = 27;

	@Override
	public void run() {
		try {
			boolean flag = false;
			while (!flag) {
				DigitalNumber dn = numQueue.take();
				if (dn.getLine1() == null && dn.getLine2() == null && dn.getLine3() == null) {
					flag = true;
					continue;
				}
				processInput(dn);
			}
			latch.countDown();

		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * processInput is using a map to check if the the input sequence is matching to
	 * any number or not.
	 * 
	 * @param dn
	 */
	public String processInput(DigitalNumber dn) {

		if (dn.getLine1() == null || dn.getLine2() == null || dn.getLine3() == null) {
			return "????????? "+ILL;
		} else {
			int line1Len = dn.getLine1().length();
			int line2Len = dn.getLine2().length();
			int line3Len = dn.getLine3().length();
			String number;
			if (line1Len >= LEGAL_LENGTH && line2Len >= LEGAL_LENGTH && line3Len >= LEGAL_LENGTH) {
				number = process(dn, LEGAL_LENGTH / 3);
			} else {
				int minlen = Math.min(Math.min(line1Len, line2Len), line3Len);
				number = process(dn, minlen / 3);
				int legalDigits = LEGAL_LENGTH / 3;
				int actualDigits = number.length();
				StringBuffer buf = new StringBuffer();
				if (legalDigits > actualDigits) {
					for (int k = 0; k < legalDigits - actualDigits; k++) {
						buf.append("?");
					}
					number = number + buf.toString();
				}
			}

			
			if (number.contains("?")) {
				number = number + " " + ILL;
				log.info(number);
			} else {
				log.info(number);
			}
			return number;
		}

	}

	private String process(DigitalNumber dn, int len) {
		int beginIndex = 0;
		int endIndex = 3;
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < len; i++) {
			String ch = dn.getLine1().substring(beginIndex, endIndex)
					+ dn.getLine2().substring(beginIndex, endIndex) + dn.getLine3().substring(beginIndex, endIndex);
			beginIndex = endIndex;
			endIndex = endIndex + 3;
			String digit = SymbolMatcher.matchSymbol(ch);
			buff.append(digit);
		}
		return buff.toString();
	}
		

}
