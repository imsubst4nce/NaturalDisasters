package dom2app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import engine.IMainController;
import engine.MainController;

public class SingleMeasureRequest implements ISingleMeasureRequest {
	private String requestName;
	private String requestCountryName;
	private String requestIndicator;
	private int requestStartYear;
	private int requestEndYear;
	private MeasurementVector requestResult;
	private boolean isAnswered = false;
	private List<IMeasurementVector> vectorList;
	private ArrayList<Pair<Integer,Integer>> allMeasurements = new ArrayList<Pair<Integer,Integer>>();
	
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
				this.isAnswered = true;
				this.requestResult = (MeasurementVector)v;
				
				if((this.requestStartYear >= 1980) && (this.requestEndYear >= 1980) && (this.requestStartYear < this.requestEndYear))
					setRequestYearRange();
				
				return this.requestResult;
			}
		}
		
		return null; // we didn't find any corresponding vectors
	}
	
	public boolean isAnsweredFlag()	{
		return this.isAnswered;
	}
	
	// for this class only
	private void setRequestYearRange()	{	
		this.requestResult.setYearRange(this.requestStartYear, this.requestEndYear);
	}
	
	public void printRequestMeasurements()	{
		for(Pair<Integer,Integer> p:this.allMeasurements)
			System.out.println(p.getKey()+" | "+p.getValue());
	}
	
	private void calculateRequestStats()	{
		this.requestResult.calculateDescriptiveStats();
		this.requestResult.calculateRegression();
	}
	
	public String getDescriptiveStatsString()	{
		calculateRequestStats();
		return this.requestResult.getDescriptiveStatsAsString();
	}
	
	public String getRegressionResultString()	{
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
		
		ISingleMeasureRequest newReq = new SingleMeasureRequest("GR-TOT", "Greece", "Drought", 2015, 2020, vectors);
		
		((MeasurementVector)newReq.getAnswer()).printMeasurementVector();
		((SingleMeasureRequest)newReq).printRequestMeasurements();
	}

}
