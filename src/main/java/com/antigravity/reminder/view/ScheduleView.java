package com.antigravity.reminder.view;

import com.antigravity.reminder.model.Schedule;
import com.antigravity.reminder.viewmodel.ScheduleViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ScheduleView {
    private final ScheduleViewModel viewModel = new ScheduleViewModel();
    private final VBox cardsContainer = new VBox(15);
    private Schedule selectedSchedule = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void show() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("schedules-view");

        Label titleLabel = new Label("📅 Керування розкладами");
        titleLabel.getStyleClass().addAll("title-label", "schedules-title");

        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        refreshData();

        Button createBtn = new Button("➕ Новий розклад");
        createBtn.getStyleClass().add("button-primary");

        Button deleteBtn = new Button("🗑️ Видалити");
        deleteBtn.getStyleClass().add("button-danger");

        Button backBtn = new Button("⬅️ Назад");
        backBtn.getStyleClass().add("button-secondary");

        HBox controls = new HBox(15, createBtn, deleteBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(titleLabel, scrollPane, controls);
        ViewManager.showView("Розклади", root);

        createBtn.setOnAction(e -> showCreateDialog());
        deleteBtn.setOnAction(
                e -> {
                    if (selectedSchedule != null) {
                        viewModel.deleteSchedule(selectedSchedule.getId());
                        selectedSchedule = null;
                        refreshData();
                    }
                });
        backBtn.setOnAction(e -> new MainMenuView().show());
    }

    private void refreshData() {
        viewModel.refreshSchedules();
        cardsContainer.getChildren().clear();
        for (Schedule s : viewModel.getSchedules()) {
            cardsContainer.getChildren().add(createScheduleCard(s));
        }
    }

    private VBox createScheduleCard(Schedule s) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(15));
        card.setStyle("-fx-cursor: hand; -fx-background-radius: 12;");

        Label name = new Label(s.getName() != null ? s.getName() : "Розклад #" + s.getId());
        name.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox frequencyBadge = new HBox(5);
        frequencyBadge.setAlignment(Pos.CENTER);
        frequencyBadge.setStyle(
                "-fx-background-color: #10b981; -fx-background-radius: 10; -fx-padding: 2 10;");
        Label freqLabel = new Label(s.getFrequency());
        freqLabel.setStyle("-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;");
        frequencyBadge.getChildren().add(freqLabel);

        Label details =
                new Label(
                        "Інтервал: "
                                + s.getInterval()
                                + " | Початок: "
                                + s.getStartTime().format(formatter));
        details.setStyle("-fx-text-fill: #64748b;");

        card.getChildren().addAll(new HBox(10, name, frequencyBadge), details);

        card.setOnMouseClicked(
                e -> {
                    cardsContainer
                            .getChildren()
                            .forEach(
                                    n ->
                                            n.setStyle(
                                                    "-fx-cursor: hand; -fx-background-radius:"
                                                            + " 12;"));
                    card.setStyle(
                            "-fx-cursor: hand; -fx-background-radius: 12; -fx-border-color:"
                                    + " #10b981; -fx-border-width: 2;");
                    selectedSchedule = s;
                });

        return card;
    }

    private void showCreateDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Створити розклад");

        VBox content = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Назва (напр. Будні)");

        ComboBox<String> freqCombo =
                new ComboBox<>(
                        FXCollections.observableArrayList("ONCE", "DAILY", "WEEKLY", "MONTHLY"));
        freqCombo.setValue("DAILY");

        TextField intervalField = new TextField("1");
        intervalField.setPromptText("Інтервал");

        TextField startField = new TextField(LocalDateTime.now().format(formatter));
        startField.setPromptText("Час початку (рррр-мм-дд гг:хв)");

        content.getChildren()
                .addAll(
                        new Label("Назва:"), nameField,
                        new Label("Періодичність:"), freqCombo,
                        new Label("Інтервал:"), intervalField,
                        new Label("Час початку:"), startField);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait()
                .ifPresent(
                        result -> {
                            if (result == ButtonType.OK) {
                                try {
                                    viewModel.createSchedule(
                                            nameField.getText(),
                                            freqCombo.getValue(),
                                            Integer.parseInt(intervalField.getText()),
                                            LocalDateTime.parse(startField.getText(), formatter));
                                    refreshData();
                                } catch (Exception e) {
                                    Alert alert =
                                            new Alert(
                                                    Alert.AlertType.ERROR,
                                                    "Некоректне введення: " + e.getMessage());
                                    alert.show();
                                }
                            }
                        });
    }
}
