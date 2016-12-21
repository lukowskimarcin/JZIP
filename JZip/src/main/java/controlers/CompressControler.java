package controlers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.controlsfx.control.Notifications;
import org.fxbase.utils.DialogsUtil;
import org.fxbase.views.BaseControler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import services.SevenZipService;

public class CompressControler extends BaseControler implements Initializable {
	@Inject
	private SevenZipService service;

	public CompressControler() {
		super("src/main/resources/fxml/Compress.fxml");
	}
	
	private ObservableList<String> files = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileList.setItems(files);
		progress.setVisible(false);
	}
	
	@FXML
	private ProgressBar progress;


	@FXML
	private ListView<String> fileList;

	@FXML
	private TextField archive;

	@FXML
	private Button bCreate;

	@FXML
	private Button bAdd;

	@FXML
	void onAddFiles(ActionEvent event) {
		try {
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Zapisz archiwum");

			File file = fileChooser.showOpenDialog(appControler.getStage());
			if (file != null) {
				files.add(file.getAbsolutePath());
			}
			
		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		}
	}

	@FXML
	void onCreateArchive(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Zapisz archiwum");

			ExtensionFilter filter = new ExtensionFilter("Archiwa", "*.7z");
			fileChooser.getExtensionFilters().add(filter);
			fileChooser.setSelectedExtensionFilter(filter);

			File file = fileChooser.showSaveDialog(appControler.getStage());
			if (file != null) {
				
				service.addListener(e -> {
					System.out.println(e.getFileName() + "[" + String.format("[%.2f sec] !!!", e.getProgress() * 100) + " %]");
					Platform.runLater(() -> {
						progress.setVisible(true);
						progress.setProgress(e.getProgress());
					});
				});
				
				archive.setText(file.getPath());
				
				
				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						service.compress(file, fileList.getItems());
						
						Platform.runLater(() -> {
							Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT)
							.title("Kompresja...")
							.text("Ukończona")
							.showInformation();	
						});
						return null;
					}
				};
				new Thread(task).start();
			}
			
		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		}
	}

	

}
