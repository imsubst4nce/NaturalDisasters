package TestCases;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeasurementVectorTest {
	private MeasurementVector vectorToTest;
	
	@Before
	public void setUp() {
		String vector = "value1\tvalue2\tvalue3\tvalue4\tvalue5\t1\t2\t3";
		String delimiter = "\t";
		vectorToTest = new MeasurementVector(vector, delimiter);
	}
	
	@Test
	public final void testGetCountryName() {
		assertEquals("value2", vectorToTest.getCountryName());
	}
	
	@Test
	public final void testGetIndicatorString() {
		assertEquals("value4", vectorToTest.getIndicatorString());
	}
	
	@Test
	public final void testGetMeasurements() {
		assertEquals(3, vectorToTest.getMeasurements().size());
	}
	
	@Test
	public final void testCalculateDescriptiveStats() {
		vectorToTest.calculateDescriptiveStats();
		assertNotNull(vectorToTest.getDescriptiveStatsAsString());
	}
	
	@Test
	public final void testCalculateRegression() {
		vectorToTest.calculateRegression();
		assertNotNull(vectorToTest.getRegressionResultAsString());
	}
	
	@Test
    void testFillNullValuesWithNullInput() {
        String[] inputVector = null;
        assertNull(vectorToTest.fillNullValues(inputVector));
    }

    @Test
    void testSplitVectorWithNullInput() {
        String vector = null;
        assertNull(vectorToTest.splitVector(vector, "\t"));
    }

    @Test
    void testGetCountryNameWithNullInput() {
        vectorToTest = new MeasurementVector(null, "\t");
        assertNull(vectorToTest.getCountryName());
    }

    @Test
    void testGetMeasurementsWithInvalidData() {
        String vectorsec = "value1\tvalue2\tvalue3\tvalue4\tvalue5\t1\tinvalid\t3";
        vectorToTest = new MeasurementVector(vectorsec, "\t");
        assertEquals(2, vectorToTest.getMeasurements().size());
    }

    @Test
    void testCalculateDescriptiveStatsWithNoData() {
        vectorToTest.calculateDescriptiveStats();
        assertNull(vectorToTest.getDescriptiveStatsAsString());
    }

    @Test
    void testCalculateRegressionWithNoData() {
        vectorToTest.calculateRegression();
        assertNull(vectorToTest.getRegressionResultAsString());
    }

    @Test
    void testGetLabelWithUndefinedSlope() {
        String vector = "value1\tvalue2\tvalue3\tvalue4\tvalue5\t1\t2\t3";
        vectorToTest = new MeasurementVector(vector, "\t");
        assertEquals("Tendency Undefined", vectorToTest.getLabel());
    }
}
