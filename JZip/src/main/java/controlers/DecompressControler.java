package controlers;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.fxbase.views.BaseControler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import services.SevenZipService;

public class DecompressControler extends BaseControler {

	@Inject
	private SevenZipService service;

	private String lastPath = null;

	public DecompressControler() {
		super("src/main/resources/fxml/Decompres.fxml");
	}
	
	@FXML
    private ListView<String> list;
	
	 @FXML
	 private ProgressBar progress;

	 
	 
	  

	@FXML
	private Button bClear;

	@FXML
	void onClear(ActionEvent event) {
		list.getItems().clear();
		progress.setVisible(false);
		
	}
	

	public void decompress() {
		ObservableList<String> files = FXCollections.observableArrayList();
		list.setItems(files);
		
	   service.addListener( e -> {
		   System.out.println(e.getFileName() + "[" + String.format("[%.2f sec] !!!", e.getProgress()*100) + " %]");
		   Platform.runLater(() -> {
			   progress.setVisible(true);
			   files.add(e.getFileName());
			   progress.setProgress(e.getProgress());
		   });
		});
		
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
			
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						service.decompres(file, out);
						return null;
					}
				};
				new Thread(task).start();
						
			} catch (Exception ex) {
				ex.printStackTrace();
				
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
