package com.antigravity.reminder.view;

import com.antigravity.reminder.model.NotificationLog;
import com.antigravity.reminder.viewmodel.NotificationLogViewModel;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class NotificationLogView {
    private final NotificationLogViewModel viewModel = new NotificationLogViewModel();
    private final TableView<NotificationLog> table = new TableView<>();

    public void show() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("history-view");

        Label titleLabel = new Label("📜 Історія сповіщень");
        titleLabel.getStyleClass().add("title-label");

        setupTable();
        table.setItems(FXCollections.observableArrayList(viewModel.getLogs()));

        Button backBtn = new Button("⬅️ Назад");
        backBtn.getStyleClass().add("button-secondary");
        backBtn.setOnAction(e -> new MainMenuView().show());

        root.getChildren().addAll(titleLabel, table, backBtn);
        ViewManager.showView("Історія сповіщень", root);
    }

    private void setupTable() {
        TableColumn<NotificationLog, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<NotificationLog, Integer> reminderCol = new TableColumn<>("ID Нагадування");
        reminderCol.setCellValueFactory(new PropertyValueFactory<>("reminderId"));
        reminderCol.setPrefWidth(120);

        TableColumn<NotificationLog, LocalDateTime> dateCol = new TableColumn<>("Час відправки");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        dateCol.setPrefWidth(180);

        TableColumn<NotificationLog, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<NotificationLog, String> channelCol = new TableColumn<>("Канал");
        channelCol.setCellValueFactory(new PropertyValueFactory<>("channelType"));
        channelCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, reminderCol, dateCol, statusCol, channelCol);
    }
}
