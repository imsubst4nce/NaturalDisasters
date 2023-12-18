package dom2app;

import java.util.ArrayList;

public class SingleMeasureRequest implements ISingleMeasureRequest {
	private String requestName;
	private String requestCountryName;
	private String requestIndicator;
	private int requestStartYear;
	private int requestEndYear;
	private MeasurementVector requestResult;
	private boolean isAnswered;
	private ArrayList<IMeasurementVector> vectorList;
	
	// Constructor simple request
	public SingleMeasureRequest(String requestName, String requestCountryName, String requestIndicator,
			ArrayList<IMeasurementVector> vectorList)	{
		
		this.requestName = requestName;
		this.requestCountryName = requestCountryName;
		this.requestIndicator = requestIndicator;
		this.vectorList = vectorList;
		
	}
	
	// Constructor year range request
	public SingleMeasureRequest(String requestName, String requestCountryName, String requestIndicator, int startYear, int endYear,
			ArrayList<IMeasurementVector> vectorList)	{
		
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
		return this.requestCountryName.concat(this.requestIndicator);
	}
	
	public IMeasurementVector getAnswer()	{
		/*for(IMeasurementVector v:vectorList) {
			if(v.getCountryName().equals(this.requestCountryName) && v.getIndicatorString().equals(this.requestIndicator))	{
				isAnswered = true;
				this.requestResult = v;
				return v;
			}
		}
		isAnswered = false;
		return null; // we didn't find any corresponding vectors*/
		for(int i = 0; i < vectorList.size(); i++)	{
			if(vectorList.get(i).getCountryName().equals(this.requestCountryName) && vectorList.get(i).getIndicatorString().equals(this.requestIndicator))	{
				isAnswered = true;
				this.requestResult = vectorList.get(i);
				return this.requestResult;
			}
		}
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
	\**********************/
	// for testing purposes
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
