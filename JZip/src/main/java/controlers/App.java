package controlers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;

import javax.enterprise.event.Observes;

import org.fxbase.cdi.StartupScene;
import org.fxbase.cdi.WeldJavaFXLauncher;
import org.fxbase.utils.DialogsUtil;
import org.fxbase.views.AppControler;
import org.fxbase.views.BaseControler;
import org.fxbase.views.JFXView;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends AppControler {
	
	public static void main(String[] args) {
		 Application.launch(WeldJavaFXLauncher.class, args);
	}
	
	public void lunch(@Observes @StartupScene Stage primaryStage) {
		launchJavaFXApplication(primaryStage);
	}
	
	@Override
	public String getTitle() {
		return "JZip";
	}
	

	@Override
	public void init() {
		try {
			InputStream img = new FileInputStream("src/main/resources/images/zip.png");
			Image icon = new Image(img);
			
			stage.getIcons().add(icon);
			DialogsUtil.defaultIcon(icon);
		 
			JFXView<BaseControler> menu = load(MenuControler.class);
			setTopNode(menu);
			
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}
