package dom2app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.*;
import app.examples.SimpleUsageApacheMath;
import org.apache.commons.math3.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/*
 * H class MeasurementVector ylopoiei to IMeasurementVector interface pou mas dinetai etoimo
 * me kapoies extra methodous pou prosthetoume emeis gia na kanoume thn anakthsh twn dedomenwn
 * apo to arxeio.
*/
public class MeasurementVector implements IMeasurementVector{
	private String[] split_vector; // h grammh xwrismenh me vash ton delimiter
	private ArrayList<Pair<Integer,Integer>> mCountryIndicator = null;
	private DescriptiveStatistics stats = null;
	private long countYearsWithEvents = 0; // years having at least 1 event helpful for the stats
	private SimpleRegression regression = null;
	
	// Constructor
	public MeasurementVector(String vector, String delimiter)	{
		this.split_vector = splitVector(vector, delimiter);
		this.split_vector = fillNullValues(this.split_vector);
	}
	
	public MeasurementVector()	{
		
	}
	
	// voithitiki methodos pou gemizei ta kena me midenika
	public String[] fillNullValues(String[] split_vector)	{
		for(int i = 0; i < split_vector.length; i++) {
			if(split_vector[i] == "")	{
				split_vector[i] = "0";
			}
		}
		
		return split_vector;
	}
	
	// spaw to string me vash ton delimiter kai gemizoume ta null pedia me midenika
	public String[] splitVector(String vector, String delimiter)	{
		return vector.split(delimiter,-1); // limit -1 gia na to kanei gia ola ta stoixeia
	}
	
	// getter gia to onoma ths xwras
	public String getCountryName() {
		return this.split_vector[1];
	}
	
	// getter gia ton typo ths katastrofis
	public String getIndicatorString() {
		return this.split_vector[4];
	}
	
	// epistrefei ena arraylist me pairs apo <year, number of events>
	// gia to sigkekrimeno <country, indicator>
	public ArrayList<Pair<Integer, Integer>> getMeasurements() {
		ArrayList<Pair<Integer,Integer>> mCountryIndicator = new ArrayList<Pair<Integer,Integer>>();
		int year = 1980; // first year
		
		Pair<Integer,Integer> yearNumberOfEvents = new Pair<Integer,Integer>(year,Integer.parseInt(this.split_vector[5]));
		mCountryIndicator.add(yearNumberOfEvents);
		year += 1; // increase year by one
		
		for(int i = 6; i < this.split_vector.length; i++)	{
			yearNumberOfEvents = new Pair<Integer,Integer>(year,Integer.parseInt(split_vector[i]));
			mCountryIndicator.add(yearNumberOfEvents);
			year += 1;
		}
		
		this.mCountryIndicator = mCountryIndicator; // create a field for later use
		
		return mCountryIndicator;
	}
	
	// edw kanoume olous tous ypologismous twn vasikwn
	// statistikwn
	public void calculateDescriptiveStats()	{
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		// gather all the data needed
		for(Pair<Integer, Integer> p:this.mCountryIndicator)	{
			stats.addValue(p.getValue());
			if(p.getValue() > 0)
				this.countYearsWithEvents++;
		}
		
		this.stats = stats;
	}
	
	// metatrepw ta stats se String kai to epistrefw
	public String getDescriptiveStatsAsString() {
		String statsString = "Descriptive Statistics\n" 
				+ "----------------------\n";
		
		statsString += "\nTotal measurements: " + this.stats.getN();
		statsString += "\nMaximum events: " + this.stats.getMax();
		statsString += "\nMinimum events: " + this.stats.getMin();
		statsString += "\nMean: " + this.stats.getMean();
		statsString += "\nGeometric Mean: " + this.stats.getGeometricMean();
		statsString += "\nMedian: " + this.stats.getPercentile(50);
		statsString += "\nKurtosis: " + this.stats.getKurtosis();
		statsString += "\nStandard Deviation: " + this.stats.getStandardDeviation();
		statsString += "\nTotal events: " + this.stats.getSum();
		statsString += "\nYears with events: " + this.countYearsWithEvents;
		
		return statsString;
	}
	
	// edw ypologizoume to regression
	public void calculateRegression()	{
		SimpleRegression regression = new SimpleRegression();
		
		for(Pair<Integer,Integer> p:this.mCountryIndicator) {
			regression.addData(p.getKey(),p.getValue());
		}
		
		this.regression = regression;
	}
	
	public String getLabel() {
        double slope = this.regression.getSlope();
        
        if (Double.isNaN(slope)) {
            return "Tendency Undefined";
        } else if (slope > 0.1) {
            return "Increased Tendency";
        } else if (slope < -0.1) {
            return "Decreased Tendency";
        } else {
            return "Tendency stable";
        }
    }
	
	// metatrepw to apotelesma tou regression se String kai to epistrefw
	public String getRegressionResultAsString() {
		String regressionString = "Regression\n"
				+ "----------\n";
		
		regressionString += "\nSlope: " + this.regression.getSlope();
		regressionString += "\nIntercept: " + this.regression.getIntercept();
		regressionString += "\nSlopeError: " + this.regression.getSlopeStdErr();
		regressionString += "\nTendency: " + getLabel();
		
		return regressionString;	
	}
	
	// just a simple print statement for our measurementvector
	public void printMeasurementVector()	{
		for(String e:this.split_vector)	{
			System.out.print(e+" | ");
		}
		System.out.println();
	}
	
	/**********************************\
	|**********************************|
	\**********************************/
	// simple test for our class
	public static void main(String[] args)	{
		String line = null;
		String delimiter = "\t";
		
		MeasurementVector row;
		// load input measurementvector
		try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/InputData/ClimateRelatedDisasters.tsv"))){
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
			line = reader.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		row = new MeasurementVector(line, delimiter);
		
		System.out.println(row.getCountryName());
		System.out.println(row.getIndicatorString());
		System.out.println();
		
		row.printMeasurementVector();
		System.out.println();
		
		ArrayList<Pair<Integer,Integer>> row_measurements = row.getMeasurements();
		for(Pair<Integer,Integer> p:row_measurements) {
			System.out.println(p);
		}
		System.out.println();
		
		row.calculateDescriptiveStats();
		System.out.println(row.getDescriptiveStatsAsString());
		System.out.println();
		
		row.calculateRegression();
		System.out.println(row.getRegressionResultAsString());
		
	}

}
