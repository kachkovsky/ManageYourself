package com.kachkovsky.busyhistory;

import com.kachkovsky.javafx.WindowHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //stage.setTitle(WindowHelper.DEFAULT_TITLE);
        //stage.setMaximized(true);
        WindowHelper.showStageForFXML(this.getClass(), stage, "/fxml/form/table.fxml");
    }

    @Override
    public void stop() {
        System.out.println("Stage closing");
        // Save file
    }
}