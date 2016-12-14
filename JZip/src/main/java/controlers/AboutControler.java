package controlers;

import org.fxbase.views.BaseControler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutControler extends BaseControler {

	public AboutControler() {
		super("src/main/resources/fxml/About.fxml");
	}
	
	private Stage dialogStage;
	

    @FXML
    private Button bOk;

    @FXML
    void onAction(ActionEvent event) {
    	 dialogStage.close();
    }

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

    
    
}
