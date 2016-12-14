package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

public class SevenZipService {
	
	public void decompres(File file, File out) throws IOException {
		SevenZFile sevenZFile = new SevenZFile(file);
	    SevenZArchiveEntry entry = sevenZFile.getNextEntry();
	    
	    while(entry!=null){
	        String path = out.getAbsolutePath() + "/" +  entry.getName();
	        System.out.println(path);
	        
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
	    }
	    sevenZFile.close();
	}
	
	
	public void compress(File file) {
		
	}
	

}
