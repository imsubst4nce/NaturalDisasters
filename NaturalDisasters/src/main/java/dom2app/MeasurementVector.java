package dom2app;

import java.util.ArrayList;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/*
 * H class MeasurementVector ylopoiei to IMeasurementVector interface pou mas dinetai etoimo
 * me kapoies extra methodous pou prosthetoume emeis gia na kanoume thn anakthsh twn dedomenwn
 * apo to arxeio.
*/
public class MeasurementVector implements IMeasurementVector{
	private String[] split_vector; // h grammh xwrismenh me vash ton delimiter
	private ArrayList<Pair<Integer,Integer>> mCountryIndicator = new ArrayList<Pair<Integer,Integer>>();
	private DescriptiveStatistics stats = null;
	private long countYearsWithEvents = 0; // years having at least 1 event helpful for the stats
	private SimpleRegression regression = null;
	private int startingYear = 0;
	private int endingYear = 0;
	
	// Constructor
	public MeasurementVector(String vector, String delimiter)	{
		this.split_vector = splitVector(vector, delimiter);
		this.split_vector = fillNullValues(this.split_vector);
	}
	
	public MeasurementVector()	{
		
	}
	
	// voithitiki methodos pou gemizei ta kena me midenika
	public String[] fillNullValues(String[] split_vector)	{
		if(split_vector != null)	{
			for(int i = 0; i < split_vector.length; i++) {
				if(split_vector[i].equals(""))	{
					split_vector[i] = "0";
				}
			}
			return split_vector;
		}
		return null; // split_vector is null
	}
	
	// spaw to string me vash ton delimiter kai gemizoume ta null pedia me midenika
	public String[] splitVector(String vector, String delimiter)	{
		if(vector != null)
			return vector.split(delimiter,-1); // limit -1 gia na to kanei gia ola ta stoixeia
		return null; // vector is null
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
		ArrayList<Pair<Integer,Integer>> totalMeasurements = new ArrayList<Pair<Integer,Integer>>();
		Pair<Integer,Integer> yearNumberOfEvents;
		int year = 1980; // first year
		if(!this.mCountryIndicator.isEmpty())
			this.mCountryIndicator.clear();; // clear the list
		
		// get all measurements
		for(int i = 5; i < this.split_vector.length; i++)	{
			yearNumberOfEvents = new Pair<Integer,Integer>(year,Integer.parseInt(split_vector[i]));
			totalMeasurements.add(yearNumberOfEvents);
			year += 1;
		}
		
		// if we have a simple find without year range
		// return the whole ArrayList
		if(this.startingYear == 0 && this.endingYear == 0)	{
			this.mCountryIndicator =  totalMeasurements;
		// else we return only the ones in the year range
		} else {
			// case <country,indicator,year-range>
			for(Pair<Integer,Integer> p:totalMeasurements)	{
				if((p.getKey() >= this.startingYear) && (p.getKey() <= this.endingYear))	{
					this.mCountryIndicator.add(new Pair<Integer,Integer>(p.getKey(),p.getValue()));
				}
			}
		}
		
		return this.mCountryIndicator;
	}
	
	public void setYearRange(int startingYear, int endingYear)	{
		this.startingYear = startingYear;
		this.endingYear = endingYear;
	}
	
	// edw kanoume olous tous ypologismous twn vasikwn
	// statistikwn gia olo to vector
	public void calculateDescriptiveStats()	{
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		this.countYearsWithEvents = 0; // clear previous value
		
		// gather all the data needed
		for(Pair<Integer, Integer> p:this.mCountryIndicator)	{
			stats.addValue(p.getValue());
			if(p.getValue() > 0)
				this.countYearsWithEvents++;
		}
		
		this.stats = stats;
	}
	
	// edw kanoume olous tous ypologismous twn statistikwn
	// mesa se ena year range
	public void calculateDescriptiveStats(ArrayList<Pair<Integer, Integer>> yearRangeMeasurements)	{
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		this.countYearsWithEvents = 0; // clear previous value
		
		// gather all the data needed
		for(Pair<Integer, Integer> p:yearRangeMeasurements)	{
			stats.addValue(p.getValue());
			if(p.getValue() > 0)
				this.countYearsWithEvents++;
		}
		
		this.stats = stats;
	}
	
	
	// metatrepw ta stats se String kai to epistrefw
	public String getDescriptiveStatsAsString() {
		String statsString = "Descriptive Statistics\n";
		
		statsString += "Total measurements: " + this.stats.getN() + "  ";
		statsString += "\nMaximum events: " + this.stats.getMax() + "  ";
		statsString += "\nMinimum events: " + this.stats.getMin() + "  ";
		statsString += "\nMean: " + this.stats.getMean() + "  ";
		statsString += "\nGeometric Mean: " + this.stats.getGeometricMean() + "  ";
		statsString += "\nMedian: " + this.stats.getPercentile(50) + "  ";
		statsString += "\nKurtosis: " + this.stats.getKurtosis() + "  ";
		statsString += "\nStandard Deviation: " + this.stats.getStandardDeviation() + "  ";
		statsString += "\nTotal events: " + this.stats.getSum() + "  ";
		statsString += "\nYears with events: " + this.countYearsWithEvents + "  \n";
		
		return statsString;
	}
	
	// edw ypologizoume to regression gia olo to vector
	public void calculateRegression()	{
		SimpleRegression regression = new SimpleRegression();
		
		for(Pair<Integer,Integer> p:this.mCountryIndicator) {
			regression.addData(p.getKey(),p.getValue());
		}
		
		this.regression = regression;
	}
	
	// edw ypologizoume to regression gia to sigkekrimeno year-range
	public void calculateRegression(ArrayList<Pair<Integer, Integer>> yearRangeMeasurements)	{
		SimpleRegression regression = new SimpleRegression();
		
		for(Pair<Integer,Integer> p:yearRangeMeasurements) {
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
		String regressionString = "Regression\n";
		
		regressionString += "Slope: " + this.regression.getSlope() + "  ";
		regressionString += "\nIntercept: " + this.regression.getIntercept()+ "  ";
		regressionString += "\nSlopeError: " + this.regression.getSlopeStdErr()+ "  ";
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
}
