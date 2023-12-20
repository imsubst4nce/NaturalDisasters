package engine;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.util.Pair;
import dom2app.IMeasurementVector;
import dom2app.ISingleMeasureRequest;
import dom2app.MeasurementVector;
import dom2app.SingleMeasureRequest;

// O main controller einai ypefthynos gia tin ylopoihsh twn use cases
public class MainController implements IMainController{
	private List<IMeasurementVector> load;
	private List<ISingleMeasureRequest> requests = new ArrayList<ISingleMeasureRequest>();
	
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

	public ISingleMeasureRequest findSingleCountryIndicator(String requestName, String countryName, String indicatorString)
			throws IllegalArgumentException{
		ISingleMeasureRequest singleMeasureRequest = new SingleMeasureRequest(requestName, countryName, indicatorString, this.load);

		if(singleMeasureRequest != null) {
			this.requests.add(singleMeasureRequest);
		}
		
		return singleMeasureRequest;
	}

	public ISingleMeasureRequest findSingleCountryIndicatorYearRange(String requestName, String countryName,
			String indicatorString, int startYear, int endYear) throws IllegalArgumentException{
		
		ISingleMeasureRequest singleMeasureRequestYear = new SingleMeasureRequest(requestName, countryName, indicatorString,
				startYear, endYear, this.load);
		if(singleMeasureRequestYear != null){
			this.requests.add(singleMeasureRequestYear);
		}
		
		return singleMeasureRequestYear;
	}
	
	public Set<String> getAllRequestNames(){
		Set<String> requestNames = new HashSet<String>();
		
		for(ISingleMeasureRequest request:requests)	{
			requestNames.add(request.getRequestName());
		}
		
		return requestNames;
	}

	public ISingleMeasureRequest getRequestByName(String requestName) {
		for(ISingleMeasureRequest request:requests)	{
			if(request.getRequestName().equals(requestName))
				return request;
		}
		
		return null;
	}

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

	public int reportToFile(String outputFilePath, String requestName, String reportType) throws IOException {
		ISingleMeasureRequest existingRequest = getRequestByName(requestName);
		Path path = Paths.get(outputFilePath);
		
		if(reportType == "text")	{
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
				
				writer.write("Request name: " + "\"" + requestName + "\""+ "\n\n");
				writer.write("<Country-Indicator>: " + "<" + existingRequest.getRequestFilter() + ">" + "\n\n");
				
				List<Pair<Integer,Integer>> measurements = existingRequest.getAnswer().getMeasurements();
				
				writer.write("Measurements" + "\n" + "------------" + "\n");
				for(Pair<Integer,Integer> p:measurements)	{
					writer.write("Year: " + p.getKey() + " | Events: " + p.getValue() + "\n");
				}
				
				writer.write("\n"+existingRequest.getDescriptiveStatsString().toString()+"\n\n");
				writer.write(existingRequest.getRegressionResultString().toString()+"\n\n");
				
				writer.close();
				
				return ((int)Files.lines(path).count());
			} catch (IOException e) {
				System.err.println("An error occured.");
				e.printStackTrace();
				return -1;
			}
		} else if(reportType == "md") {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
				
				writer.write("**Request name: " + "\"" + requestName + "\"**"+ "\n\n");
				writer.write("_\\<Country-Indicator\\>: " + "\\<" + existingRequest.getRequestFilter() + "\\>_" + "\n\n");
				
				List<Pair<Integer,Integer>> measurements = existingRequest.getAnswer().getMeasurements();
			
				writer.write("| Year | Events |\n");
				writer.write("|------|--------|\n");
				
				for(Pair<Integer,Integer> p:measurements)	{
					writer.write("|"+p.getKey() + "|" + p.getValue() + "|\n");
				}
				
				writer.write("\n"+existingRequest.getDescriptiveStatsString().toString()+"\n\n");
				writer.write(existingRequest.getRegressionResultString().toString()+"\n\n");
				
				writer.close();
				
				return ((int)Files.lines(path).count());
			} catch (IOException e) {
				System.err.println("An error occured.");
				e.printStackTrace();
				return -1;
			}
		} else if(reportType == "html")	{
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
				
				writer.write("<!doctype html>\n");
				writer.write("<html>\n");
				writer.write("<head>\n");
				writer.write("<meta http-equiv=\"Content-Type\"content \"text\\html; charset=windows-1253\">\n");
				writer.write("<title>Natural Disaster Data</title>");
				writer.write("<head>\n");
				writer.write("<body>\n");
				
				writer.write("<p><b>Request name: " + requestName + "</br></p>\n");
				
				writer.write("<p><i>Country-Indicator: " + existingRequest.getRequestFilter() + "</i></p>\n");
				
				List<Pair<Integer,Integer>> measurements = existingRequest.getAnswer().getMeasurements();
				
				writer.write("<table>\n");
				
				writer.write("<tr>\n");
				writer.write("<td>Year</td> <td>Events</td>\n");
				writer.write("</tr>\n");
				
				for(Pair<Integer,Integer> p:measurements)	{
					writer.write("<tr>");
					writer.write("<td>"+ p.getKey() + "</td> <td>" + p.getValue() + "</td>\n");
					writer.write("</tr>");
				}
				
				writer.write("</table>\n\n");
				
				writer.write("<p>" + existingRequest.getDescriptiveStatsString().toString() + "\n");
				writer.write("<p>" + existingRequest.getRegressionResultString().toString() + "\n</body>\n</html>");
				
				writer.close();
				
				return ((int)Files.lines(path).count());
			} catch (IOException e) {
				System.err.println("An error occured.");
				e.printStackTrace();
				return -1;
			}
		}
		
		return -1;
	}
	
	public List<ISingleMeasureRequest> getRequests() {
		return this.requests;
	}
}
