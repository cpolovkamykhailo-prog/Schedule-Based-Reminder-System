package com.antigravity.reminder.view;

import com.antigravity.reminder.model.Reminder;
import com.antigravity.reminder.viewmodel.ReminderViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ReminderView {
    private final ReminderViewModel viewModel = new ReminderViewModel();
    private final VBox listContainer = new VBox(10);
    private final VBox detailView = new VBox(20);
    private Reminder selectedReminder = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void show() {
        SplitPane splitPane = new SplitPane();
        splitPane.getStyleClass().add("reminders-view");

        // --- LEFT SIDE: MASTER LIST ---
        VBox masterSide = new VBox(15);
        masterSide.setPadding(new Insets(20));
        masterSide.setMinWidth(300);
        masterSide.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("🔔 Нагадування");
        titleLabel.getStyleClass().addAll("title-label", "reminders-title");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Пошук...");
        searchField
                .textProperty()
                .addListener((obs, oldVal, newVal) -> updateList(viewModel.search(newVal)));

        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Button backBtn = new Button("⬅️ Меню");
        backBtn.getStyleClass().add("button-secondary");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setOnAction(e -> new MainMenuView().show());

        masterSide.getChildren().addAll(titleLabel, searchField, scrollPane, backBtn);

        // --- RIGHT SIDE: DETAILS ---
        detailView.setPadding(new Insets(40));
        detailView.setAlignment(Pos.TOP_LEFT);
        detailView.setStyle("-fx-background-color: #f8fafc;");

        showEmptyState();

        splitPane.getItems().addAll(masterSide, detailView);
        splitPane.setDividerPositions(0.35);

        refreshData();

        ViewManager.showView("Керування нагадуваннями", splitPane);
    }

    private void refreshData() {
        viewModel.refreshReminders();
        updateList(viewModel.getReminders());
    }

    private void updateList(java.util.List<Reminder> reminders) {
        listContainer.getChildren().clear();
        for (Reminder r : reminders) {
            listContainer.getChildren().add(createCompactItem(r));
        }
    }

    private VBox createCompactItem(Reminder r) {
        VBox item = new VBox(5);
        item.setPadding(new Insets(12));
        item.getStyleClass().add("card");
        item.setStyle(
                "-fx-cursor: hand; -fx-background-radius: 8; "
                        + (r.isCompleted() ? "-fx-opacity: 0.5;" : ""));

        Label title = new Label(r.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label time = new Label(r.getRemindAt().format(formatter));
        time.setStyle("-fx-text-fill: #6366f1; -fx-font-size: 12px;");

        item.getChildren().addAll(title, time);

        item.setOnMouseClicked(e -> showDetails(r));
        return item;
    }

    private void showDetails(Reminder r) {
        selectedReminder = r;
        detailView.getChildren().clear();

        Label t = new Label(r.getTitle());
        t.getStyleClass().add("title-label");
        t.setStyle("-fx-font-size: 32px;");

        Label desc = new Label(r.getDescription());
        desc.setWrapText(true);
        desc.setStyle("-fx-font-size: 16px; -fx-text-fill: #475569;");

        Label time = new Label("📅 Заплановано на: " + r.getRemindAt().format(formatter));
        time.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #6366f1;");

        HBox actions = new HBox(15);
        Button editBtn = new Button("➕ Створити нове");
        editBtn.getStyleClass().add("button-primary");
        editBtn.setOnAction(e -> showCreateDialog());

        Button completeBtn = new Button(r.isCompleted() ? "✅ Виконано" : "⭕ Позначити як виконане");
        completeBtn.getStyleClass().add("button-secondary");
        completeBtn.setOnAction(
                e -> {
                    viewModel.markAsCompleted(r.getId());
                    refreshData();
                    // Fetch updated reminder
                    viewModel.getReminders().stream()
                            .filter(rem -> rem.getId() == r.getId())
                            .findFirst()
                            .ifPresent(this::showDetails);
                });

        Button delBtn = new Button("🗑️ Видалити");
        delBtn.getStyleClass().add("button-danger");
        delBtn.setOnAction(
                e -> {
                    viewModel.deleteReminder(r.getId());
                    refreshData();
                    showEmptyState();
                });

        actions.getChildren().addAll(editBtn, completeBtn, delBtn);
        detailView.getChildren().addAll(t, new Separator(), desc, time, actions);
    }

    private void showEmptyState() {
        detailView.getChildren().clear();
        Label placeholder = new Label("Виберіть нагадування зі списку зліва 👈");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #94a3b8;");

        Button createBtn = new Button("➕ Створити перше нагадування");
        createBtn.getStyleClass().add("button-primary");
        createBtn.setOnAction(e -> showCreateDialog());

        detailView.getChildren().addAll(placeholder, createBtn);
    }

    private void showCreateDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Створити нагадування");
        dialog.setHeaderText("Введіть деталі нагадування");

        VBox content = new VBox(10);
        TextField titleField = new TextField();
        titleField.setPromptText("Заголовок");
        TextArea descField = new TextArea();
        descField.setPromptText("Опис");
        descField.setPrefRowCount(3);
        TextField dateField = new TextField(LocalDateTime.now().format(formatter));
        dateField.setPromptText("Дата (рррр-мм-дд гг:хв)");
        TextField scheduleIdField = new TextField("0");
        scheduleIdField.setPromptText("ID розкладу (0 якщо немає)");

        content.getChildren()
                .addAll(
                        new Label("Заголовок:"), titleField,
                        new Label("Опис:"), descField,
                        new Label("Нагадати о:"), dateField,
                        new Label("ID розкладу:"), scheduleIdField);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait()
                .ifPresent(
                        result -> {
                            if (result == ButtonType.OK) {
                                try {
                                    viewModel.createReminder(
                                            titleField.getText(),
                                            descField.getText(),
                                            LocalDateTime.parse(dateField.getText(), formatter),
                                            Integer.parseInt(scheduleIdField.getText()));
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
