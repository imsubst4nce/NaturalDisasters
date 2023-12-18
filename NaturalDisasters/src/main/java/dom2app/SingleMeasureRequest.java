package dom2app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import engine.IMainController;
import engine.MainController;

public class SingleMeasureRequest implements ISingleMeasureRequest {
	private String requestName;
	private String requestCountryName;
	private String requestIndicator;
	private int requestStartYear;
	private int requestEndYear;
	private MeasurementVector requestResult;
	private boolean isAnswered;
	private List<IMeasurementVector> vectorList;
	
	// Constructor simple request
	public SingleMeasureRequest(String requestName, String requestCountryName, String requestIndicator,
			List<IMeasurementVector> vectorList)	{
		
		this.requestName = requestName;
		this.requestCountryName = requestCountryName;
		this.requestIndicator = requestIndicator;
		this.vectorList = vectorList;
		
	}
	
	// Constructor year range request
	public SingleMeasureRequest(String requestName, String requestCountryName, String requestIndicator, int startYear, int endYear,
			List<IMeasurementVector> vectorList)	{
		
		this.requestName = requestName;
		this.requestCountryName = requestCountryName;
		this.requestIndicator = requestIndicator;
		this.requestStartYear = startYear;
		this.requestEndYear = endYear;
		this.vectorList = vectorList;
		
	}
	
	// returns request name
	public String getRequestName()	{
		return this.requestName;
	}
	
	// returns request's "CountryName-Indicator"
	public String getRequestFilter()	{
		return this.requestCountryName.concat(" "+this.requestIndicator);
	}
	
	public IMeasurementVector getAnswer()	{
		for(IMeasurementVector v:vectorList) {
			if(v.getCountryName().equals(this.requestCountryName) && v.getIndicatorString().equals(this.requestIndicator))	{
				isAnswered = true;
				this.requestResult = (MeasurementVector)v;
				return v;
			}
		}
		isAnswered = false;
		return null; // we didn't find any corresponding vectors
	}
	
	public boolean isAnsweredFlag()	{
		return isAnswered;
	}
	
	// for this class only
	private void getRequestMeasurements()	{
		this.requestResult.getMeasurements();
	}
	
	private void calculateRequestStats()	{
		this.requestResult.calculateDescriptiveStats();
		this.requestResult.calculateRegression();
	}
	
	public String getDescriptiveStatsString()	{
		getRequestMeasurements();
		calculateRequestStats();
		return this.requestResult.getDescriptiveStatsAsString();
	}
	
	public String getRegressionResultString()	{
		getRequestMeasurements();
		calculateRequestStats();
		return this.requestResult.getRegressionResultAsString();
	}
	
	/**********************\
	|**********************|
	|**********************|
	\
	 * @throws IOException 
	 * @throws FileNotFoundException **********************/
	// for testing purposes
	public static void main(String[] args) throws FileNotFoundException, IOException {
		IMainController mainController = new MainController();
		
		List<IMeasurementVector> vectors = mainController.load("src/main/resources/InputData/ClimateRelatedDisasters.tsv", "\t");
		
		ISingleMeasureRequest newReq = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", vectors);
		
		((MeasurementVector)newReq.getAnswer()).printMeasurementVector();
		System.out.println(newReq.isAnsweredFlag());
		System.out.println(newReq.getDescriptiveStatsString());
		System.out.println(newReq.getRegressionResultString());
		
	}

}
