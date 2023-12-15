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
		Pair<Integer,Integer> yearNumberOfEvents = new Pair<Integer,Integer>(Integer.parseInt(this.splited_vector[5]),Integer.parseInt(this.splited_vector[6]));
		
		for(int i = 7; i < this.splited_vector.length-1; i++)	{
			yearNumberOfEvents = new Pair<Integer,Integer>(Integer.parseInt(splited_vector[i]), Integer.parseInt(splited_vector[i+1]));
			mCountryIndicator.add(yearNumberOfEvents);
		}
		
		return mCountryIndicator;
	}
	
	// to-do
	public String getDescriptiveStatsAsString() {
		String stats = "";
		
		return stats;
	}
	
	// to-do
	public String getRegressionResultAsString() {
		String regressionResult = "";
		
		return regressionResult;
	}
}
