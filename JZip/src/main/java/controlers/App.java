package controlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import fxbase.AbstractJavaFxApplication;
import javafx.scene.image.Image;

 

@Lazy
@ComponentScan(basePackages= {"controlers", "services"})
@SpringApplicationConfiguration
public class App extends AbstractJavaFxApplication {
	
	protected static final Logger log = Logger.getLogger(App.class.getName());   
	
	public static void main(String[] args) throws FileNotFoundException {
		launchApp(App.class, MainControler.class, args);
	}
	 
	

	@Override
	protected List<Image> loadIcons() {
		List<Image> images = new ArrayList<>();
		try {
			InputStream img = new FileInputStream("src/main/resources/images/zip.png");
			Image icon = new Image(img);
			
			images.add(icon);
			
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return images;
	}
	 
}
