package controlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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

@FXMLView("/fxml/Menu.fxml")
public class MenuControler extends AbstractView {

	@Autowired
	private App app;
	
	@Autowired
	private MainControler main;
	
	@Autowired
	private AboutControler about;
	
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
	@SuppressWarnings("unchecked")
	void onAbout(ActionEvent event) throws FileNotFoundException {
		Stage stage = new Stage();
		stage.setTitle("O programie");
		stage.initOwner(app.getStage());
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(false);
		stage.getIcons().add(app.getStage().getIcons().get(0));
     	 
     	about.reload();
		about.setDialogStage(stage);
		
		Scene scene = new Scene(about.getView());
		
		stage.setScene(scene);
		stage.showAndWait();
		
	}

	@FXML
	void onCompress(ActionEvent event) {
		CompressControler view = loadView(CompressControler.class);
		main.setCenter(view);
	}

	
	@FXML
	void onDecompress(ActionEvent event) {
		DecompressControler view = loadView(DecompressControler.class);
		main.setCenter(view);
		
		view.decompress();
	}

}
