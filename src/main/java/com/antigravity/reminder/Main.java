package com.antigravity.reminder;

import com.antigravity.reminder.db.DatabaseInitializer;
import com.antigravity.reminder.service.NotificationService;
import com.antigravity.reminder.view.AuthView;
import com.antigravity.reminder.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private NotificationService.NotificationScheduler scheduler;

    @Override
    public void start(Stage primaryStage) {
        DatabaseInitializer.runMigrations();

        scheduler = new NotificationService.NotificationScheduler();
        scheduler.start();

        ViewManager.setPrimaryStage(primaryStage);
        new AuthView().showLogin();
    }

    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
