package dom2app;

public class ReportToHTMLFile implements IReport{
	private String outputFilePath; 
	private String requestName;
	
	public ReportToHTMLFile(String outputFilePath, String requestName)	{
		this.outputFilePath = outputFilePath;
		this.requestName = requestName;
	}

	public boolean reportToFile()	{
		
	}
	
	public int getLinesWritten() {
		
	}
}
