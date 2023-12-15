package dom2app;

import java.util.ArrayList;

import org.apache.commons.math3.util.Pair;

/*
 * H class MeasurementVector ylopoiei to IMeasurementVector interface pou mas dinetai etoimo
 * me kapoies extra methodous pou prosthetoume emeis gia na kanoume thn anakthsh twn dedomenwn
 * apo to arxeio.
*/
public class MeasurementVector implements IMeasurementVector{
	private String[] splited_vector; // h grammh xwrismenh me vash ton delimiter
	
	// Constructor
	public MeasurementVector(String vector, String delimiter)	{
		this.splited_vector = splitVector(vector, delimiter);
	}

	// Edw spaw to string me vash ton delimiter
	public String[] splitVector(String vector, String delimiter)	{
		String[] splited_row = vector.split(delimiter);
		
		return splited_row;
	}
	
	// getter gia to onoma ths xwras
	public String getCountryName() {
		return splited_vector[1];
	}
	
	// getter gia ton typo ths katastrofis
	public String getIndicatorString() {
		return splited_vector[4];
	}
	
	// epistrefei ena arraylist me pairs apo <year, number of events>
	// gia to sigkekrimeno <country, indicator>
	public ArrayList<Pair<Integer, Integer>> getMeasurements() {
		ArrayList<Pair<Integer,Integer>> mCountryIndicator = new ArrayList<Pair<Integer,Integer>>();
		int year = 1980; // first year
		
		Pair<Integer,Integer> yearNumberOfEvents = new Pair<Integer,Integer>(year,Integer.parseInt(this.splited_vector[5]));
		mCountryIndicator.add(yearNumberOfEvents);
		year += 1; // increase year by one
		
		for(int i = 6; i < this.splited_vector.length; i++)	{
			yearNumberOfEvents = new Pair<Integer,Integer>(year,Integer.parseInt(splited_vector[i]));
			mCountryIndicator.add(yearNumberOfEvents);
			year += 1;
		}
		
		return mCountryIndicator;
	}
	
	// edw kanoume olous tous ypologismous twn vasikwn
	// statistikwn
	public ArrayList<Integer> calculateDescriptiveStats()	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		return list; 
	}
	
	// metatrepw ta stats se String kai to epistrefw
	public String getDescriptiveStatsAsString() {
		String stats = "";
		
		return stats;
	}
	// edw ypologizoume to regression
	public ArrayList<Double> calculateRegression()	{
		ArrayList<Double> list = new ArrayList<Double>();
		
		return list;
	}
	
	// metatrepw to apotelesma tou regression se String kai to epistrefw
	public String getRegressionResultAsString() {
		String regressionResult = "";
		
		return regressionResult;
	}
}
