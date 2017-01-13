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
	
	@FXML
	private MenuItem mCompress;

	@FXML
	private MenuItem mDecompress;

	@FXML
	private MenuItem mClose;

	@FXML
	private MenuItem mAbout;

	@Autowired
	AboutControler aboutView;
	
	@FXML
	void closeAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	@SuppressWarnings("unchecked")
	void onAbout(ActionEvent event) throws FileNotFoundException {
		// = loadView(AboutControler.class);
		aboutView.setTitle("O programie");
		aboutView.initOwner(app.getStage()) 
 			.initModality(Modality.WINDOW_MODAL)
 			//.center()
 			.showAndWait();
 		
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
