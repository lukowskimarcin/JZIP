package controlers;

import java.awt.Desktop;
import java.io.File;
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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
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
	private MenuItem mOpen;

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

		service.addListener(e -> {
			System.out.println(e.getFileName() + "[" + String.format("[%.2f sec] !!!", e.getProgress() * 100) + " %]");
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

		if (file != null) {
			try {
				lastPath = file.getParent();

				DirectoryChooser saveFileChooser = new DirectoryChooser();
				saveFileChooser.setTitle("Rozpakuj w ...");
				File out = saveFileChooser.showDialog(appControler.getStage());

				Task<Void> task = new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						service.decompres(file, out);
						
						Platform.runLater(() -> {
							Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT)
							.title("Rozpakowywanie...")
							.text("Ukończone")
							.showInformation();	
						});
						return null;
					}
				};
				new Thread(task).start();

			} catch (Exception ex) {
				DialogsUtil.create()
					.title("Brak pliku")
					.showException(ex);
			}

		} else {
			DialogsUtil.create()
				.title("Brak pliku")
				.message("Nie wybrano pliku!!!")
				.showWarning();
		}
	}

	@FXML
	void onOpen(ActionEvent event) {
		String path = list.getSelectionModel().getSelectedItem();
		try {
			File file = new File(path);
			
			Desktop.getDesktop().open(file);
		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		} 
	}

}
