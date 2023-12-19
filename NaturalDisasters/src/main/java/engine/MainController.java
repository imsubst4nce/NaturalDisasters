package engine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dom2app.IMeasurementVector;
import dom2app.IReport;
import dom2app.ISingleMeasureRequest;
import dom2app.MeasurementVector;
import dom2app.ReportToHTMLFile;
import dom2app.ReportToMarkdownFile;
import dom2app.ReportToTextFile;
import dom2app.SingleMeasureRequest;

// O main controller einai ypefthynos gia tin ylopoihsh twn use cases
public class MainController implements IMainController{
	private List<IMeasurementVector> load;
	private List<ISingleMeasureRequest> requests = new ArrayList<ISingleMeasureRequest>();
	
	/*
	 * Takes a structured text file as input and converts its contents to a List<MeasurementVector>  
	 * 
	 * @param fileName a String with the path of the file to load
	 * @param delimiter a String that denotes the delimiter of the fields inside each  line (e.g., "\t" or ",", or "|")
	 * @return a List<IMeasurementVector> with each line of the file represented as a MeasurementVector object
	 * @throws FileNotFoundException when a fileName specified is not present as a file
	 * @throws IOException if sth goes wrong during the reading of the input file
	 * @see dom2app.IMeasurementVector
	 */
	public List<IMeasurementVector> load(String fileName, String delimiter) throws FileNotFoundException, IOException{
		ArrayList<IMeasurementVector> vectorList = new ArrayList<IMeasurementVector>();
		String line = null;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
			reader.readLine(); // ignore 1st line that contains category names
			while((line = reader.readLine()) != null) {
				MeasurementVector row = new MeasurementVector(line, delimiter);
				vectorList.add(row);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.load = vectorList; // keep the loaded measurements for later use
		
		return vectorList; // rowlist[0] einai oi titloi twn keliwn
	}

	/*
	 * A request to the server that selects a specific country and a specific indicator 
	 * 
	 * @param requestName a String with the name that the request will take
	 * @param countryName a String with the name of the country
	 * @param indicatorString a String with the name of the indicator
	 * @return a new ISingleMeasureRequest with the result of the request 
	 * @throws IllegalArgumentException if any of the arguments is an empty string
	 * @see dom2app.ISingleMeasureRequest
	 */
	public ISingleMeasureRequest findSingleCountryIndicator(String requestName, String countryName, String indicatorString)
			throws IllegalArgumentException{
		ISingleMeasureRequest singleMeasureRequest = new SingleMeasureRequest(requestName, countryName, indicatorString, this.load);

		if(singleMeasureRequest != null){
			this.requests.add(singleMeasureRequest);
		}
		return singleMeasureRequest;
	}

	/*
	 * A request to the server that selects a specific country and a specific indicator for a time range
	 * 
	 * The semantics of the time range are closed [startYear,endYear]
	 * 
	 * @param requestName a String with the name that the request will take
	 * @param countryName a String with the name of the country
	 * @param indicatorString a String with the name of the indicator
	 * @param startYear an Integer with the first year of the range that interests us (included) 
	 * @param endYear an Integer with the last year of the range that interests us (included) 
	 * @return a new ISingleMeasureRequest with the result of the request 
	 * @throws IllegalArgumentException if any of the arguments is an empty string, or, end < start year
	 * @see dom2app.ISingleMeasureRequest
	 */
	public ISingleMeasureRequest findSingleCountryIndicatorYearRange(String requestName, String countryName,
			String indicatorString, int startYear, int endYear) throws IllegalArgumentException{
		
		ISingleMeasureRequest singleMeasureRequestYear = new SingleMeasureRequest(requestName, countryName, indicatorString,
				startYear, endYear, this.load);
		if(singleMeasureRequestYear != null){
			this.requests.add(singleMeasureRequestYear);
		}
		return singleMeasureRequestYear;
	}
	
	/*
	 * A set of strings with all the request names made so far to the server
	 * 
	 * We use the term find* requests to refer to all the requests that dig up ISingleMeasureRequest from the loaded collection.
	 * The server is obliged to keep track of every find* request, and be able to return the names of these requests
	 * 
	 * @return A set of strings with all the request names made so far to the server
	 */
	public Set<String> getAllRequestNames(){
		Set<String> requestNames = new HashSet<String>();
		
		for(ISingleMeasureRequest request:requests)	{
			requestNames.add(request.getRequestName());
		}
		
		return requestNames;
	}

	/*
	 * Asks the server to find a specific request and return it as an ISingleMeasureRequest
	 * @param requestName A String with the request name
	 * @return an ISingleMeasureRequest object with the retrieved request's data; or null if the request does not exist
	 * @see dom2app.ISingleMeasureRequest
	 */
	public ISingleMeasureRequest getRequestByName(String requestName) {
		for(ISingleMeasureRequest request:requests)	{
			if(request.getRequestName().equals(requestName))
				return request;
		}
		
		return null;
	}

	/*
	 * Computes the regression for the data of the request and returns an updated ISingleMeasureRequest
	 * 
	 * @param requestName A String with the name of the request
	 * @return an updated ISingleMeasureRequest with regression data, if the respective request exists and has an IMeasurementVector with data; null otherwise
	 * @see dom2app.ISingleMeasureRequest
	 */
	public ISingleMeasureRequest getRegression(String requestName) {
		ISingleMeasureRequest existingRequest = getRequestByName(requestName);
		
		if(existingRequest != null)	{
			if(existingRequest.isAnsweredFlag() == true)	{
				existingRequest.getRegressionResultString();
				return existingRequest;
			}
		}
		
		return null;
	}

	/*
	 * Computes the descriptive stats for the data of the request and returns an updated ISingleMeasureRequest
	 * 
	 * @param requestName A String with the name of the request
	 * @return an updated ISingleMeasureRequest with descriptive stats, if the respective request exists and has an IMeasurementVector with data; null otherwise
	 * @see dom2app.ISingleMeasureRequest
	 */
	public ISingleMeasureRequest getDescriptiveStats(String requestName) {
		ISingleMeasureRequest existingRequest = getRequestByName(requestName);
		
		if(existingRequest != null)	{
			if(existingRequest.isAnsweredFlag() == true)	{
				existingRequest.getDescriptiveStatsString();
				return existingRequest;
			}
		}
		
		return null;
	}

	/*
	 * Outputs the contents of an existing request in a report file.
	 * 
	 * The user needs to specify the path and the type of the report.
	 * The report type can be either text, or markdown, or html.
	 * 
	 * @param outputFilePath A String with the path of the output file
	 * @param requestName A String with the name of the request to be reported
	 * @param reportType A String that is either "text" for text, "md" for markdown, "html" for html
	 * @return an integer with the number of lines written; -1 if sth goes wrong
	 * @throws IOException if sth goes wrong during the writing of the output file
	 */
	public int reportToFile(String outputFilePath, String requestName, String reportType) throws IOException {
		IReport newReport = null;
		int linesWritten;
		
		if(reportType == "text")	{
			newReport = new ReportToTextFile(outputFilePath, requestName);
		} else if(reportType == "md") {
			newReport = new ReportToMarkdownFile(outputFilePath, requestName);
		} else if(reportType == "html")	{
			newReport = new ReportToHTMLFile(outputFilePath, requestName);
		}
		
		linesWritten = newReport.getLinesWritten();
		
		return linesWritten;
	}
	
	public List<ISingleMeasureRequest> getRequests() {
		return this.requests;
	}
	
	//public void exitProgram()	{
		
	//}
	
	public static void main(String[] args) throws FileNotFoundException, IOException	{
		IMainController mainController = new MainController();
		
		List<IMeasurementVector> vectors = mainController.load("src/main/resources/InputData/ClimateRelatedDisasters.tsv", "\t");
		
		ISingleMeasureRequest newReq = mainController.findSingleCountryIndicatorYearRange("GR-TOT", "Greece", "TOTAL", 2019, 2020);
		
		//((MeasurementVector)mainController.getRequestByName("GR-TOT").getAnswer()).printMeasurementVector();
		
		for(ISingleMeasureRequest req: ((MainController)mainController).getRequests())	{
			((MeasurementVector)req.getAnswer()).printMeasurementVector();
			((SingleMeasureRequest)req).printRequestMeasurements();
		}
		
		System.out.println(mainController.getDescriptiveStats("GR-TOT").getDescriptiveStatsString());
		System.out.println(mainController.getRegression("GR-TOT").getRegressionResultString());
	}
	
}
