package com.antigravity.reminder.view;

import com.antigravity.reminder.viewmodel.AuthViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainMenuView {
    private final AuthViewModel authViewModel = new AuthViewModel();

    public void show() {
        VBox root = new VBox(30);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Вітаємо, " + authViewModel.getCurrentUsername() + "! 👋");
        welcomeLabel.getStyleClass().add("title-label");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Button remindersBtn = createDashboardButton("🔔 Керування нагадуваннями", "reminders-btn");
        remindersBtn.setOnAction(e -> new ReminderView().show());

        Button schedulesBtn = createDashboardButton("📅 Керування розкладами", "schedules-btn");
        schedulesBtn.setOnAction(e -> new ScheduleView().show());

        Button tagsBtn = createDashboardButton("🏷️ Керування тегами", "tags-btn");
        tagsBtn.setOnAction(e -> new TagView().show());

        Button historyBtn = createDashboardButton("📜 Історія сповіщень", "history-btn");
        historyBtn.setOnAction(e -> new NotificationLogView().show());

        Button profileBtn = createDashboardButton("⚙️ Мій профіль", "profile-btn");
        profileBtn.getStyleClass().add("button-secondary");
        profileBtn.setOnAction(e -> new ProfileView().show());

        Button logoutBtn = createDashboardButton("🚪 Вийти", "logout-btn");
        logoutBtn.getStyleClass().add("button-danger");
        logoutBtn.setOnAction(
                e -> {
                    authViewModel.logout();
                    new AuthView().showLogin();
                });

        grid.add(remindersBtn, 0, 0);
        grid.add(schedulesBtn, 1, 0);
        grid.add(tagsBtn, 0, 1);
        grid.add(historyBtn, 1, 1);
        grid.add(profileBtn, 0, 2);
        grid.add(logoutBtn, 1, 2);

        root.getChildren().addAll(welcomeLabel, grid);
        ViewManager.showView("Головне меню", root);
    }

    private Button createDashboardButton(String text, String styleClass) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("button", "menu-button", styleClass);
        if (!styleClass.equals("logout-btn")) {
            btn.getStyleClass().add("button-secondary");
        }
        return btn;
    }
}
