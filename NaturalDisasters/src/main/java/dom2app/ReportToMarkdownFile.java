package dom2app;

public class ReportToMarkdownFile implements IReport{
	private String outputFilePath; 
	private String requestName;
	
	public ReportToMarkdownFile(String outputFilePath, String requestName)	{
		this.outputFilePath = outputFilePath;
		this.requestName = requestName;
	}

	public boolean reportToFile()	{
		
	}
	
	public int getLinesWritten() {
		
	}
}
