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
    void btnCopyOnAction(ActionEvent event) throws InterruptedException {
        btnCopy.getScene().getWindow().setHeight(650);
        if (new File(target, source.getName()).exists() && !source.getParentFile().equals(target)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, String.format("%s file is already exist Do you want to replace", source.getName()), ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isEmpty() || confirm.get() == ButtonType.NO) return;
        }

        btnCopy.getScene().getWindow().setHeight(300);
        if (source.isFile()) {
            fileWriter(source, target);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("'%s' file is copied to '%s' ", source.getName(), target.getName()));
            Optional<ButtonType> buttonType = alert.showAndWait();
            Thread.sleep(300);
            btnCopy.getScene().getWindow().setHeight(220);
            clearSelection();
            return;
        }

        folderWriter(source, target);
        new Alert(Alert.AlertType.INFORMATION, String.format("'%s' file is copied to '%s' ", source.getName(), target.getName())).show();
        Thread.sleep(300);
        btnCopy.getScene().getWindow().setHeight(220);
        clearSelection();
    }
    private void fileWriter(File source, File target) {
        try {
            FileInputStream fis = new FileInputStream(source);
            File tempFile = new File(target, ".temp.txt");
            FileOutputStream fos = new FileOutputStream(tempFile);
            long size = source.length();
            long progress=0;
            while (true) {
                progress+=1*1024;
                byte[] buffer = new byte[1024 * 1];
                int read = fis.read(buffer);
                if (read == -1) break;
                fos.write(buffer, 0, read);
                setProgressBar(progress,size);
            }

            fis.close();
            fos.close();;
            if (source.getParentFile().equals(target)) {
                int i = 1;
                String k = String.valueOf(i);
                String name = source.getName().substring(0, source.getName().lastIndexOf("."));
                String format = source.getName().substring(source.getName().lastIndexOf("."));
                while (true) {
                    File fileNew = new File(target, name.concat(String.valueOf(i)).concat(format));
                    if (!fileNew.exists()) {
                        tempFile.renameTo(fileNew);
                        return;
                    }
                    i++;
                }
            }
            tempFile.renameTo(new File(target, source.getName()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSelection() {
        txtSource.clear();
        txtTargets.clear();
        btnBrowsTarget.setDisable(true);
        btnCopy.setDisable(true);
        btnMove.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void folderWriter(File source, File target) {
        new File(target, source.getName()).mkdirs();
        File[] files = source.listFiles();
        for (File selectFile : files) {
            if (selectFile.isFile()) {
                fileWriter(selectFile, (new File(target, source.getName())));
                continue;
            }
            new File(target, selectFile.getName()).mkdirs();
            folderWriter(selectFile, new File(target, source.getName()));
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        System.out.println(source);
        if (!source.exists()) return;
        if (source.isFile()) {
            source.delete();
            new Alert(Alert.AlertType.INFORMATION, String.format("'%s' file is deleted", source.getName())).show();
            clearSelection();
            return;
        }
        folderDelete(source);
        new Alert(Alert.AlertType.INFORMATION, String.format("'%s' file is deleted", source.getName())).show();
        clearSelection();
        System.out.println("deletedFolder");
    }

    private void folderDelete(File source) {
        File[] files = source.listFiles();
        for (File selectFile : files) {
            if (selectFile.isFile()) {
                selectFile.delete();
                continue;
            }
            folderDelete(selectFile);
        }
        source.delete();
        System.out.println("deleted");
    }

    @FXML
    void btnMoveOnAction(ActionEvent event) {}

    private void setProgressBar(long current,long target) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage((current/target*100) +"% Complete");
                updateProgress(current/target*100,100);
                return null;
            }
        };
        new Thread(task).start();
        pgbr.progressProperty().bind(task.progressProperty());
        lblProgress.textProperty().bind(task.messageProperty());

    }




}
