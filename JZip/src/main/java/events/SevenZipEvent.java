package events;

public class SevenZipEvent {
	
	private String fileName;
	
	private double progress;
	
	
	public SevenZipEvent(String fileName, double progress) {
		this.fileName = fileName;
		this.progress = progress;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	

}
