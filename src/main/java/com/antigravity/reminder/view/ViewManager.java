package com.antigravity.reminder.view;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewManager {
    private static Stage primaryStage;
    private static final Map<String, Parent> views = new HashMap<>();

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void showView(String name, Parent pane) {
        if (primaryStage == null) return;

        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(pane, 900, 700);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(pane);
        }

        var cssResource = ViewManager.class.getResource("/styles.css");
        if (cssResource != null) {
            String css = cssResource.toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
        }

        primaryStage.setTitle("Reminder System - " + name);
        primaryStage.show();
    }
}
