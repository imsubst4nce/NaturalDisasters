import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
 
import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;

class MainControllerTest {

    private IMainController mainController;

    @Before
    void setUp() {
        mainController = new MainController();
    }

    @Test
    void testLoad() {
        // Assuming you have a sample data file for testing
        String fileName = "path/to/test/data/file.tsv";
        String delimiter = "\t";

        try {
            List<IMeasurementVector> loadedData = mainController.load(fileName, delimiter);

            assertNotNull(loadedData);
            assertFalse(loadedData.isEmpty());
            // Add more assertions based on the specific structure of your data
        } catch (IOException e) {
            fail("IOException not expected during file loading");
        }
    }

    @Test
    void testFindSingleCountryIndicator() {
        String requestName = "TestRequest";
        String countryName = "TestCountry";
        String indicatorString = "TestIndicator";

        try {
            ISingleMeasureRequest request = mainController.findSingleCountryIndicator(requestName, countryName, indicatorString);

            assertNotNull(request);
            assertEquals(requestName, request.getRequestName());
            assertEquals(countryName, request.getCountryName());
            assertEquals(indicatorString, request.getIndicatorString());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException not expected for valid input");
        }
    }

    @Test
    void testFindSingleCountryIndicatorYearRange() {
        String requestName = "TestRequest";
        String countryName = "TestCountry";
        String indicatorString = "TestIndicator";
        int startYear = 2000;
        int endYear = 2020;

        try {
            ISingleMeasureRequest request = mainController.findSingleCountryIndicatorYearRange(requestName, countryName, indicatorString, startYear, endYear);

            assertNotNull(request);
            assertEquals(requestName, request.getRequestName());
            assertEquals(countryName, request.getCountryName());
            assertEquals(indicatorString, request.getIndicatorString());
            assertEquals(startYear, request.getStartYear());
            assertEquals(endYear, request.getEndYear());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException not expected for valid input");
        }
    }

    @Test
    void testGetAllRequestNames() {
        // Assuming you have made some requests
        mainController.findSingleCountryIndicator("Request1", "Country1", "Indicator1");
        mainController.findSingleCountryIndicatorYearRange("Request2", "Country2", "Indicator2", 2000, 2020);

        Set<String> requestNames = mainController.getAllRequestNames();

        assertNotNull(requestNames);
        assertTrue(requestNames.contains("Request1"));
        assertTrue(requestNames.contains("Request2"));
    }

    @Test
    void testGetRequestByName() {
        // Assuming you have made a request
        mainController.findSingleCountryIndicator("TestRequest", "TestCountry", "TestIndicator");

        ISingleMeasureRequest request = mainController.getRequestByName("TestRequest");

        assertNotNull(request);
        assertEquals("TestRequest", request.getRequestName());
    }

    @Test
    void testGetRegression() {
        // Assuming you have made a request and it has been answered
        mainController.findSingleCountryIndicator("TestRequest", "TestCountry", "TestIndicator");

        ISingleMeasureRequest regressionRequest = mainController.getRegression("TestRequest");

        assertNotNull(regressionRequest);
        assertTrue(regressionRequest.isAnsweredFlag());
        assertNotNull(regressionRequest.getRegressionResultString());
    }

    @Test
    void testGetDescriptiveStats() {
        // Assuming you have made a request and it has been answered
        mainController.findSingleCountryIndicator("TestRequest", "TestCountry", "TestIndicator");

        ISingleMeasureRequest statsRequest = mainController.getDescriptiveStats("TestRequest");

        assertNotNull(statsRequest);
        assertTrue(statsRequest.isAnsweredFlag());
        assertNotNull(statsRequest.getDescriptiveStatsString());
    }

    @Test
    void testReportToFile() {
        // Assuming you have made a request and it has been answered
        mainController.findSingleCountryIndicator("TestRequest", "TestCountry", "TestIndicator");

        try {
            int linesWritten = mainController.reportToFile("path/to/test/output/report.txt", "TestRequest", "text");

            assertTrue(linesWritten > 0);
        } catch (IOException e) {
            fail("IOException not expected during report generation");
        }
    }

    @Test
    void testReportToFileInvalidReportType() {
        // Assuming you have made a request and it has been answered
        mainController.findSingleCountryIndicator("TestRequest", "TestCountry", "TestIndicator");

        try {
            int linesWritten = mainController.reportToFile("path/to/test/output/report.txt", "TestRequest", "invalidType");

            fail("IllegalArgumentException should have been thrown for invalid report type");
        } catch (IOException e) {
            fail("IOException not expected during report generation");
        } catch (IllegalArgumentException e) {
            // Expected exception for invalid report type
        }
    }
    
    @Test
    void load_NullFileName_ShouldThrowFileNotFoundException() {
        assertThrows(FileNotFoundException.class, () -> mainController.load(null, "\t"));
    }

    @Test
    void findSingleCountryIndicator_NullRequestName_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicator(null, "Greece", "TOTAL"));
    }

    @Test
    void findSingleCountryIndicator_NullCountryName_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicator("GR-TOT", null, "TOTAL"));
    }

    @Test
    void findSingleCountryIndicator_NullIndicatorString_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicator("GR-TOT", "Greece", null));
    }

    @Test
    void findSingleCountryIndicatorYearRange_NullRequestName_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicatorYearRange(null, "Greece", "TOTAL", 2015, 2020));
    }

    @Test
    void findSingleCountryIndicatorYearRange_NullCountryName_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicatorYearRange("GR-TOT", null, "TOTAL", 2015, 2020));
    }

    @Test
    void findSingleCountryIndicatorYearRange_NullIndicatorString_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicatorYearRange("GR-TOT", "Greece", null, 2015, 2020));
    }

    @Test
    void findSingleCountryIndicatorYearRange_EndYearBeforeStartYear_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> mainController.findSingleCountryIndicatorYearRange("GR-TOT", "Greece", "TOTAL", 2020, 2015));
    }

    @Test
    void getRequestByName_NullRequestName_ShouldReturnNull() {
        assertNull(mainController.getRequestByName(null));
    }

    @Test
    void getRegression_NullRequestName_ShouldReturnNull() {
        assertNull(mainController.getRegression(null));
    }

    @Test
    void getDescriptiveStats_NullRequestName_ShouldReturnNull() {
        assertNull(mainController.getDescriptiveStats(null));
    }

    @Test
    void reportToFile_NullOutputFilePath_ShouldThrowIOException() {
        assertThrows(IOException.class, () -> mainController.reportToFile(null, "GR-TOT", "text"));
    }

    @Test
    void reportToFile_NullRequestName_ShouldThrowIOException() {
        assertThrows(IOException.class, () -> mainController.reportToFile("output.txt", null, "text"));
    }

    @Test
    void reportToFile_InvalidReportType_ShouldThrowIOException() {
        assertThrows(IOException.class, () -> mainController.reportToFile("output.txt", "GR-TOT", "invalidType"));
    }
}
