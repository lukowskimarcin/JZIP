package controlers;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;

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
	void onAbout(ActionEvent event) throws FileNotFoundException {
		aboutView.setTitle("O programie");
		aboutView.initOwner(app.getStage()) 
 			.initModality(Modality.WINDOW_MODAL)
 			.center()
 			.showAndWait();

		System.out.println("showAndWait");
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
