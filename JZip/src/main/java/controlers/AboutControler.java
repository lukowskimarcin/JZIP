package controlers;



import org.springframework.context.annotation.Scope;

import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

@FXMLView("/fxml/About.fxml")
public class AboutControler extends AbstractView {

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
