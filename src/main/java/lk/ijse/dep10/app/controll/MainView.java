package lk.ijse.dep10.app.controll;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Optional;

public class MainView {

    private static File source;
    private static File target;
    @FXML
    private Button btnBrowsSource;
    @FXML
    private Button btnBrowsTarget;
    @FXML
    private Button btnCopy;

    @FXML
    private Label lblProgress;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnMove;
    @FXML
    private ProgressBar pgbr;
    @FXML
    private TextField txtSource;
    @FXML
    private TextField txtTargets;

    public void initialize() {
        btnBrowsSource.requestFocus();
    }

    @FXML
    void btnBrowsSourceOnAction(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showOpenDialog(null);
        File source = chooser.getSelectedFile();
        if (source == null) return;
        txtSource.setText(String.valueOf(source));
        MainView.source = source;
        if (!txtSource.getText().isEmpty()) {
            btnBrowsTarget.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    @FXML
    void btnBrowsTargetOnAction(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File target = chooser.getSelectedFile();
        if (target == null) return;
        MainView.target = target;
        txtTargets.setText(String.valueOf(target));
        if (!txtTargets.getText().isEmpty()) {
            btnCopy.setDisable(false);
            btnMove.setDisable(false);
        }
    }

    @FXML
    void btnCopyOnAction(ActionEvent event) throws InterruptedException {}

    private void fileWriter(File source, File target) {}

    private void folderWriter(File source, File target) {}

    @FXML
    void btnDeleteOnAction(ActionEvent event) {}

    private void folderDelete(File source) {}

    @FXML
    void btnMoveOnAction(ActionEvent event) {}





}
