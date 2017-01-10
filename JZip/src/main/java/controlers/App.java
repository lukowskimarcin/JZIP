package controlers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import fxbase.AbstractJavaFxApplication;
import javafx.scene.image.Image;
import utils.DialogsUtil;

 

@Lazy
@ComponentScan(basePackages= {"controlers", "services"})
@SpringApplicationConfiguration
public class App extends AbstractJavaFxApplication {
	
	protected static final Logger log = Logger.getLogger(App.class.getName());   
	
	public static void main(String[] args) {
		launchApp(App.class, MainControler.class, args);
	}
	

	@Override
	protected void initialize() {
		try {
			InputStream img = new FileInputStream("src/main/resources/images/zip.png");
			Image icon = new Image(img);
			
			getStage().getIcons().add(icon);
			DialogsUtil.defaultIcon(icon);
			 
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
		
	}
	 
}
