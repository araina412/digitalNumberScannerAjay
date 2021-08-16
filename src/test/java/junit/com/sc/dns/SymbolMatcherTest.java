package junit.com.sc.dns;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.sc.dns.SymbolMatcher;

public class SymbolMatcherTest {
	@Test
	public final void testSymbolMatcher() {
		String line1 = " _ ";
		String line2 = "| |";
		String line3 = "|_|";
		int num0 = Integer.valueOf(SymbolMatcher.matchSymbol(line1+line2+line3));
		assertTrue(num0 == 0);
		
		String line5 = " _ ";
		String line6 = "|_ ";
		String line7 = " _|";
		int num5 = Integer.valueOf(SymbolMatcher.matchSymbol(line5+line6+line7));
		assertTrue(num5 == 5);
		
	}
}
