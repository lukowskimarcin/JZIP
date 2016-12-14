package controlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import org.fxbase.views.BaseControler;
import org.fxbase.views.JFXView;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.SevenZipService;

public class MenuControler extends BaseControler {

	public MenuControler() {
		super("src/main/resources/fxml/Menu.fxml");
	}
	
	@Inject
	SevenZipService service;

	@FXML
	private MenuItem mCompress;

	@FXML
	private MenuItem mDecompress;

	@FXML
	private MenuItem mClose;

	@FXML
	private MenuItem mAbout;

	@FXML
	void closeAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void onAbout(ActionEvent event) throws FileNotFoundException {
		Stage stage = new Stage();
		stage.setTitle("O programie");
		stage.initOwner(appControler.getStage());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		
		JFXView<AboutControler> about =  appControler.load(AboutControler.class);
		about.getControler().setDialogStage(stage);
		
		AnchorPane anchorPane = (AnchorPane)about.getNode();
		Scene scene = new Scene(anchorPane);
		
		stage.setScene(scene);
		stage.showAndWait();
	}
		
		

	@FXML
	void onCompress(ActionEvent event) {

	}

	String lastPath = null;
	
	@FXML
	void onDecompress(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Otwórz archiwum");
		if (lastPath != null) {
			fileChooser.setInitialDirectory(new File(lastPath));
		}
		ExtensionFilter filter = new ExtensionFilter("Archiwa", "*.7z");
		fileChooser.getExtensionFilters().add(filter);
		fileChooser.setSelectedExtensionFilter(filter);
		
		File file = fileChooser.showOpenDialog(appControler.getStage());
		
		if(file != null) {
			try {
				lastPath = file.getParent();
				
				DirectoryChooser  saveFileChooser = new DirectoryChooser();
				saveFileChooser.setTitle("Rozpakuj w ...");
				File out = saveFileChooser.showDialog(appControler.getStage());
				
				service.decompres(file, out);
			} catch (Exception ex) {
				
				Alert alert = new  Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Error");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}
			
		} else {
			Alert alert = new  Alert(AlertType.WARNING);
			alert.setHeaderText(null);
			alert.setTitle("Brak pliku");
			alert.setContentText("Nie wybrano pliku!!!");
			alert.showAndWait();
		}
	}

}
