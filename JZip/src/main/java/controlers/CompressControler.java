package controlers;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.apache.commons.io.FileSystemUtils;
import org.controlsfx.control.Notifications;
import org.fxbase.utils.DialogsUtil;
import org.fxbase.views.BaseControler;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
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
	private Button bClear;

	@FXML
	private Button bAddFiles;

	@FXML
	private Button bAddCatalog;

	@FXML
	void onClear(ActionEvent event) {
		files.clear();
	}

	@FXML
	void onAddFiles(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Wskaż pliki...");

			List<File> selected = fileChooser.showOpenMultipleDialog(appControler.getStage());
			if (selected != null) {
				for (File f : selected) {
					files.add(f.getAbsolutePath());
				}
			}
		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		}
	}

	@FXML
	void onAddCatalog(ActionEvent event) {
		try {
			DirectoryChooser dirChooser = new DirectoryChooser();
			dirChooser.setTitle("Wskaż katalog...");

			File selected = dirChooser.showDialog(appControler.getStage());
			if (selected != null) {

				
				Service<Void> taskService = new Service<Void>() {
					@Override
					protected Task<Void> createTask() {
						return new Task<Void>() {
							@Override
							protected Void call() throws Exception {
								HashSet<String> set = new HashSet<String>();
								fetchFiles(selected, set);

								for (String f : set) {
									Platform.runLater(() -> {
										files.add(f);
									});
								}
								return null;
							}
						};
					}
				};
				
				appControler.getStage().getScene().cursorProperty().bind(
						Bindings.when(taskService.runningProperty()).then(Cursor.WAIT).otherwise(Cursor.DEFAULT)
				);
				taskService.start();
			}

		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		}
	}

	private void fetchFiles(File root, HashSet<String> files) {
		File[] list = root.listFiles();

		files.add(root.getAbsolutePath());
		if (list != null) {
			for (File f : list) {
				if (f.isDirectory()) {
					fetchFiles(f, files);
				} else {
					files.add(f.getAbsolutePath());
				}
			}
		} else {
			return;
		}
	}

	@FXML
	void onCreateArchive(ActionEvent event) {
		try {
			bAddCatalog.setDisable(true);
			bAddFiles.setDisable(true);
			bClear.setDisable(true);
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Zapisz archiwum");

			ExtensionFilter filter = new ExtensionFilter("Archiwa", "*.7z");
			fileChooser.getExtensionFilters().add(filter);
			fileChooser.setSelectedExtensionFilter(filter);

			File file = fileChooser.showSaveDialog(appControler.getStage());
			if (file != null) {

				service.addListener(e -> {
					System.out.println(
							e.getFileName() + "[" + String.format("[%.2f sec] !!!", e.getProgress() * 100) + " %]");
					Platform.runLater(() -> {
						progress.setVisible(true);
						progress.setProgress(e.getProgress());
					});
				});

				archive.setText(file.getPath());
				
				Service<Void> taskService = new Service<Void>() {

					@Override
					protected Task<Void> createTask() {
						return new Task<Void>() {
							@Override
							protected Void call() throws Exception {
								service.compress(file, fileList.getItems());

								Platform.runLater(() -> {
									bAddCatalog.setDisable(false);
									bAddFiles.setDisable(false);
									bClear.setDisable(false);
									Notifications.create().darkStyle().position(Pos.BOTTOM_RIGHT).title("Kompresja...")
											.text("Ukończona").showInformation();
								});
								return null;
							}
						};
					}
					
				};
				
				appControler.getStage().getScene().cursorProperty().bind(
						Bindings.when(taskService.runningProperty()).then(Cursor.WAIT).otherwise(Cursor.DEFAULT)
				);
				taskService.start();
				 
			}

		} catch (Exception ex) {
			DialogsUtil.create().showException(ex);
		}
	}

}
