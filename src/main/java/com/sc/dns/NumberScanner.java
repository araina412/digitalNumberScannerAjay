package com.sc.dns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

/**
 * NumberScanner class is used to scan a number represented in digital format.
 * Each number is three lines long and has nine digits from 0-9. Composed of "_"
 * and "|".
 * 
 * @author ajay
 *
 */

public class NumberScanner {

	private static final Logger log = Logger.getLogger(NumberScanner.class);
	private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<String>();
	private static final BlockingQueue<DigitalNumber> numQueue = new LinkedBlockingQueue<DigitalNumber>();
	private static final String FILE_END = "File End";
	private static final String NEXT_NUM = "Next Number";
	private static final CountDownLatch latch = new CountDownLatch(2);
	private static final ExecutorService ex = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws IOException, InterruptedException {
		//String fileName = "C:\\Users\\ajay\\StandardCharter\\src\\test\\resources\\testInput\\example";
		String fileName = args[0];
		//log.info("Create Number Chunker");
		NumberChunker chunker = new NumberChunker(inputQueue, numQueue, latch);
		ex.execute(chunker);
		//log.info("Create Number Processor");
		InputProcessor processor = new InputProcessor(numQueue, latch);
		ex.execute(processor);
		//Read file as stream using resource try. 
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEachOrdered(line -> {
				//empty line represents a break in between two numbers as per requirement. 
				if (!line.isEmpty() && !line.isBlank()) {
					try {
						inputQueue.put(line);
					} catch (InterruptedException e) {
						log.error(e.getMessage());
					}
				} else {
					try {
						inputQueue.put(NEXT_NUM);
					} catch (InterruptedException e) {
						log.error(e.getMessage());
					}
				}
			});
			inputQueue.put(FILE_END);
			// wait for both the Chunker and Processor thread to complete their task and then shutdown the executor service.
			latch.await();
			if (!ex.isShutdown()) {
				ex.shutdown();
			}

		} catch (IOException e) {
			log.error(e.getMessage());
			ex.shutdownNow();
			
			throw e;
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			ex.shutdownNow();
			
			throw e;
		}
	}
}
