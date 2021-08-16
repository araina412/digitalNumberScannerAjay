package junit.com.sc.dns;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.sc.dns.DigitalNumber;
import com.sc.dns.NumberChunker;

public class NumberChunkerTest {
	@Test
	public final void chunkingTest() throws InterruptedException {
		BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
		BlockingQueue<DigitalNumber> numQueue = new LinkedBlockingQueue<>();
		CountDownLatch latch = new CountDownLatch(1);
		NumberChunker chunker = new NumberChunker(inputQueue, numQueue, latch);
		Thread t = new Thread(chunker);
		t.start();
		inputQueue.put("    _  _     _  _  _  _  _ ");
		inputQueue.put("  | _| _||_||_ |_   ||_||_|");
		inputQueue.put("  ||_  _|  | _||_|  ||_| _|");
		inputQueue.put("Next Number");
		inputQueue.put("    _  _     _  _  _  _  _ ");
		inputQueue.put("  | _| _||_||_ |_   ||_||_|");
		inputQueue.put("  ||_  _|  | _||_|  ||_| _|");
		inputQueue.put("File End");
		latch.await();
		// Two Numbers are entered but the Number Queue will have three and the third
		// number will be null specifying the end of Processing.
		assertTrue(numQueue.size() == 3);

	}
}
