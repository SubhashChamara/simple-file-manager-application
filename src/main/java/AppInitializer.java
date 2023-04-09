import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainView.fxml")).load()));
        primaryStage.centerOnScreen();
        primaryStage.setMinWidth(380);
        primaryStage.setHeight(225);
        primaryStage.setTitle("Simple File Manager");
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
