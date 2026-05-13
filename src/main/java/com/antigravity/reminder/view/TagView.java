package com.antigravity.reminder.view;

import com.antigravity.reminder.model.Tag;
import com.antigravity.reminder.viewmodel.TagViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TagView {
    private final TagViewModel viewModel = new TagViewModel();
    private final FlowPane tagsContainer = new FlowPane();
    private Tag selectedTag = null;

    public void show() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("tags-view");

        Label titleLabel = new Label("🏷️ Керування тегами");
        titleLabel.getStyleClass().addAll("title-label", "tags-title");

        tagsContainer.setHgap(15);
        tagsContainer.setVgap(15);
        tagsContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tagsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        refreshData();

        Button createBtn = new Button("➕ Новий тег");
        createBtn.getStyleClass().add("button-primary");

        Button deleteBtn = new Button("🗑️ Видалити");
        deleteBtn.getStyleClass().add("button-danger");

        Button backBtn = new Button("⬅️ Назад");
        backBtn.getStyleClass().add("button-secondary");

        HBox controls = new HBox(15, createBtn, deleteBtn, backBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(titleLabel, scrollPane, controls);
        ViewManager.showView("Теги", root);

        createBtn.setOnAction(e -> showCreateDialog());
        deleteBtn.setOnAction(
                e -> {
                    if (selectedTag != null) {
                        viewModel.deleteTag(selectedTag.getId());
                        selectedTag = null;
                        refreshData();
                    }
                });
        backBtn.setOnAction(e -> new MainMenuView().show());
    }

    private void refreshData() {
        viewModel.refreshTags();
        tagsContainer.getChildren().clear();
        for (Tag tag : viewModel.getTags()) {
            tagsContainer.getChildren().add(createTagChip(tag));
        }
    }

    private Label createTagChip(Tag tag) {
        Label chip = new Label(tag.getName());
        chip.setStyle(
                "-fx-background-color: "
                        + tag.getColor()
                        + "; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 15;"
                        + " -fx-font-weight: bold; -fx-cursor: hand;");

        chip.setOnMouseClicked(
                e -> {
                    // Deselect others
                    tagsContainer.getChildren().forEach(n -> n.setEffect(null));
                    // Select this
                    selectedTag = tag;
                    chip.setEffect(new DropShadow(10, Color.BLACK));
                });

        return chip;
    }

    private void showCreateDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Створити тег");

        VBox content = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Назва");
        ColorPicker colorPicker = new ColorPicker(Color.web("#3498db"));

        content.getChildren()
                .addAll(
                        new Label("Назва:"), nameField,
                        new Label("Колір:"), colorPicker);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(content);

        dialog.showAndWait()
                .ifPresent(
                        result -> {
                            if (result == ButtonType.OK) {
                                String hex =
                                        String.format(
                                                "#%02X%02X%02X",
                                                (int) (colorPicker.getValue().getRed() * 255),
                                                (int) (colorPicker.getValue().getGreen() * 255),
                                                (int) (colorPicker.getValue().getBlue() * 255));
                                viewModel.createTag(nameField.getText(), hex);
                                refreshData();
                            }
                        });
    }
}
