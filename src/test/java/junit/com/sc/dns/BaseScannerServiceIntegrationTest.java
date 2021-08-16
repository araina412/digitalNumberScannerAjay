package junit.com.sc.dns;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.sc.dns.DigitalNumber;
import com.sc.dns.InputProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for basic end-to-end functional validations.
 * Please feel free to add more test classes for the purpose of unit/integration testing of your own implementation.
 * <p>
 * Add your test cases and their expect outputs under src/test/resources testInput and testOutput directories respectively.
 * The input and output will be paired with each other if they have matching file name.
 * <p>
 * @apiNote Implement {@link #performScanning} to return list of output given an input file path.
 * Please note that this does not necessarily define the API contract of your scanner service implementation.
 */
@RunWith(Parameterized.class)
public class BaseScannerServiceIntegrationTest {

    private static final String INPUT_DIR = "testInput";
    private static final String OUTPUT_DIR = "testOutput";

    @Parameterized.Parameters(name = "{0}")
    public static List<String> inputFileNames() throws IOException {
        Path inputDirPath = Paths.get("src", "test", "resources", INPUT_DIR);
        return Files.list(inputDirPath)
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(Collectors.toList());
    }

    @Parameterized.Parameter
    public String inputFileName;

    protected List<String> performScanning(final String inputFilePath)
    {
    	List<String> outPut = new LinkedList<>();
    	try {
			Scanner sc = new Scanner(new File(inputFilePath));
			while (sc.hasNextLine())
			{
				String line1 = sc.nextLine();
				if (line1.isEmpty() || line1.isBlank())
				{
					continue;
				}
				String line2 = sc.nextLine();
				String line3 = sc.nextLine();
				DigitalNumber num = new DigitalNumber(line1, line2, line3);
				InputProcessor processor = new InputProcessor(null, null);
				String out = processor.processInput(num);
				outPut.add(out);
				
			}
		} 
    	catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	return outPut;
    }

    @Test
    public final void testScanner() throws IOException {
        String inputFilePath = Paths.get("src", "test", "resources", INPUT_DIR, inputFileName).toAbsolutePath().toString();
        List<String> expectedOutput = Files.readAllLines(Paths.get("src", "test", "resources", OUTPUT_DIR, inputFileName));
        assertThat(performScanning(inputFilePath)).containsExactlyElementsOf(expectedOutput);
    }
}
