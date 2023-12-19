package dom2app;

public class ReportToTextFile implements IReport{
	private String outputFilePath; 
	private String requestName;
	
	public ReportToTextFile(String outputFilePath, String requestName)	{
		this.outputFilePath = outputFilePath;
		this.requestName = requestName;
	}

	public boolean reportToFile()	{
		
	}
	
	public int getLinesWritten() {
		
	}
}
