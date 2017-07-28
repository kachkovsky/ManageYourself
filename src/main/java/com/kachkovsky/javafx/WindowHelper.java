package com.kachkovsky.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowHelper {

    public static void showStageForFXML(Class mainClass, Stage stage, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(mainClass.getResource(fxml));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
//        stage.setMinWidth(1024);
//        stage.setMinHeight(740);

        Object controller = loader.getController();
        if (controller instanceof ApplyStageListener) {
            ((ApplyStageListener) controller).applyStageActions(stage);
        }
        stage.show();
    }
}
