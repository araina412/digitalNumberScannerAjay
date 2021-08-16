package com.sc.dns;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * NumberChunker is responsible for chuncking the input strings to generate
 * correct numbers and then put them into processing queue.
 * 
 * @author ajay
 *
 */
public class NumberChunker implements Runnable {
	private static final Logger log = Logger.getLogger(NumberChunker.class);
	private static final String FILE_END = "File End";
	private static final String NEXT_NUM = "Next Number";
	private final CountDownLatch latch;

	public NumberChunker(BlockingQueue<String> inputQueue, BlockingQueue<DigitalNumber> numQueue,
			CountDownLatch latch) {
		super();
		this.inputQueue = inputQueue;
		this.numQueue = numQueue;
		this.latch = latch;
	}

	private final BlockingQueue<String> inputQueue;
	private final BlockingQueue<DigitalNumber> numQueue;

	@Override
	public void run() {
		try {
			boolean flag = false;
			while (!flag) {
				String line1 = inputQueue.take();
				if (checkFileEnd(line1)) {
					flag = true;
					continue;
				}
				if (isNextNumReady(line1)) {
					continue;
				}
				String line2 = inputQueue.take();
				String line3 = inputQueue.take();
				createDNAndAddToQueue(line1, line2, line3);
			}
			numQueue.put(new DigitalNumber(null, null, null));
			// all lines are chunked and now the thread can count down.
			latch.countDown();

		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * cretae a Digital Number out of input lines and add to Processing queue.
	 * 
	 * @param line1
	 * @param line2
	 * @param line3
	 * @throws InterruptedException
	 */
	private void createDNAndAddToQueue(String line1, String line2, String line3) throws InterruptedException {
		DigitalNumber num = new DigitalNumber(line1, line2, line3);
		numQueue.put(num);

	}

	/**
	 * check if the line a an empty line representing the break in between two
	 * numbers.
	 * 
	 * @param line
	 * @return
	 * @throws InterruptedException
	 */
	private boolean isNextNumReady(String line) throws InterruptedException {
		if (line.equals(NEXT_NUM)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check if the line reprersents end of input lines.
	 * 
	 * @param line
	 * @return
	 * @throws InterruptedException
	 */
	private boolean checkFileEnd(String line) throws InterruptedException {
		if (line.equals(FILE_END)) {
			return true;
		} else {
			return false;
		}

	}

}
