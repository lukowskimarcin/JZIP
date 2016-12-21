package services; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import events.SevenZipEvent;
import interfaces.IEventHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class SevenZipService {
	
	private IEventHandler<SevenZipEvent> handler;
	
	@SuppressWarnings("unused")
	public void decompres(File file, File out) throws IOException {
		SevenZFile sevenZFile = new SevenZFile(file);
	    SevenZArchiveEntry entry = sevenZFile.getNextEntry();
	    
		int totalFiles = 0;
		double actual = 0;
		
		for (SevenZArchiveEntry en : sevenZFile.getEntries()) {
			totalFiles++;
		}
	    
	    while(entry!=null){
	    	
	        String path = out.getAbsolutePath() + "\\" +  entry.getName();
	        
	        Path pathToFile = Paths.get(path);
	        Files.createDirectories(pathToFile.getParent());
	        
	        if (Files.notExists(pathToFile)) {
	        	Files.createFile(pathToFile);
		        FileOutputStream fos = new FileOutputStream(new File(path));
		        byte[] content = new byte[(int) entry.getSize()];
		        sevenZFile.read(content, 0, content.length);
		        fos.write(content);
		        fos.close();
	        }
	        
	        entry = sevenZFile.getNextEntry();
	        actual++;
	        if(handler!=null) {
	        	handler.handle(new SevenZipEvent(path, actual / totalFiles));
	        }
	        
	    }
	    sevenZFile.close();
	}
	
	public void compress(File archive, ObservableList<String> files) throws Exception {
		SevenZOutputFile sevenZOutput = new SevenZOutputFile(archive);
		
		double totalFiles = files.size();
		double actual = 0;
		
		for(String f : files) {
			File file = new File(f);
			
			SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(file, file.getName());
			sevenZOutput.putArchiveEntry(entry);
			
			byte[] stream = Files.readAllBytes(file.toPath());
			sevenZOutput.write(stream);
			
			sevenZOutput.closeArchiveEntry();
			actual++;
			if(handler!=null) {
				handler.handle(new SevenZipEvent(f, actual / totalFiles));	
			}
			
		}
		sevenZOutput.close();
	}

	public void addListener(IEventHandler<SevenZipEvent> listener){
		handler = listener;
	}
}
