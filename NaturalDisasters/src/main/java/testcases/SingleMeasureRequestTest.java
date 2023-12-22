package TestCases;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SingleMeasureRequestTest {

    private IMainController mainController;
    private List<IMeasurementVector> vectors;

    @Before
    void setUp() throws FileNotFoundException, IOException {
        mainController = new MainController();
        vectors = mainController.load("src/main/resources/InputData/ClimateRelatedDisasters.tsv", "\t");
    }

    @Test
    void testGetRequestName() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
        assertEquals("GR-TOT", request.getRequestName());
    }

    @Test
    void testGetRequestFilter() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
        assertEquals("Greece-Drought", request.getRequestFilter());
    }

    @Test
    void testGetAnswer() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
        assertNotNull(request.getAnswer());
        assertTrue(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWhenNoMatch() {
        ISingleMeasureRequest request = new SingleMeasureRequest("Nonexistent", "Country", "Indicator", vectors);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithYearRange() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", 2015, 2020, vectors);
        assertNotNull(request.getAnswer());
        assertTrue(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithInvalidYearRange() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", 2020, 2015, vectors);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetDescriptiveStatsString() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
        assertNotNull(request.getDescriptiveStatsString());
    }

    @Test
    void testGetRegressionResultString() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
        assertNotNull(request.getRegressionResultString());
    }
    
    @Test
    void testGetAnswerWithNullVectors() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", null);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithEmptyVectors() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", new ArrayList<>());
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithInvalidCountry() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "NonexistentCountry", "Drought", vectors);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithInvalidIndicator() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "NonexistentIndicator", vectors);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithNegativeYearRange() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", -5, 2020, vectors);
        assertNull(request.getAnswer());
        assertFalse(request.isAnsweredFlag());
    }

    @Test
    void testGetAnswerWithZeroYearRange() {
        ISingleMeasureRequest request = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", 2020, 2020, vectors);
        assertNotNull(request.getAnswer());
        assertTrue(request.isAnsweredFlag());
    }

    @Test
    void testGetDescriptiveStatsStringWithNoAnswer() {
        ISingleMeasureRequest request = new SingleMeasureRequest("Nonexistent", "Country", "Indicator", vectors);
        assertNull(request.getDescriptiveStatsString());
    }

    @Test
    void testGetRegressionResultStringWithNoAnswer() {
        ISingleMeasureRequest request = new SingleMeasureRequest("Nonexistent", "Country", "Indicator", vectors);
        assertNull(request.getRegressionResultString());
    }
}
