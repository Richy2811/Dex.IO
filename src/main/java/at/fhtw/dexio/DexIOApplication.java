package at.fhtw.dexio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DexIOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DexIOApplication.class.getResource("DexIOView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("DexIO");
        stage.setScene(scene);
        stage.setMinWidth(580);
        stage.setMinHeight(430);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}