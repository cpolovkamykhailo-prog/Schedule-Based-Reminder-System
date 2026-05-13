package com.antigravity.reminder.view;

import com.antigravity.reminder.model.User;
import com.antigravity.reminder.viewmodel.ProfileViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ProfileView {
    private final ProfileViewModel viewModel = new ProfileViewModel();

    public void show() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("profile-view");

        Label titleLabel = new Label("⚙️ Налаштування профілю");
        titleLabel.getStyleClass().addAll("title-label", "profile-title");

        User user = viewModel.getCurrentUser();

        VBox form = new VBox(10);
        form.setMaxWidth(400);

        Label emailLabel = new Label("Email для нагадувань:");
        TextField emailField = new TextField(user.getEmail());
        emailField.setPromptText("example@gmail.com");

        Label passLabel = new Label("Пароль додатка (App Password):");
        PasswordField passField = new PasswordField();
        passField.setText(user.getSmtpPassword() != null ? user.getSmtpPassword() : "");
        passField.setPromptText("16-значний код від Google");

        Hyperlink helpLink = new Hyperlink("Як отримати пароль додатка?");
        helpLink.setOnAction(
                e -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Довідка");
                    alert.setHeaderText("Налаштування Gmail");
                    alert.setContentText(
                            "1. Перейдіть у Google Account -> Security.\n"
                                    + "2. Увімкніть 2-Step Verification.\n"
                                    + "3. Створіть App Password для 'Mail'.\n"
                                    + "4. Скопіюйте 16-значний код сюди.");
                    alert.show();
                });

        Button saveBtn = new Button("💾 Зберегти зміни");
        saveBtn.getStyleClass().add("button-primary");
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        Button backBtn = new Button("⬅️ Назад");
        backBtn.getStyleClass().add("button-secondary");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        form.getChildren()
                .addAll(emailLabel, emailField, passLabel, passField, helpLink, saveBtn, backBtn);

        root.getChildren().addAll(titleLabel, form);
        ViewManager.showView("Профіль", root);

        saveBtn.setOnAction(
                e -> {
                    viewModel.updateProfile(emailField.getText(), passField.getText());
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "Профіль оновлено!");
                    success.show();
                });

        backBtn.setOnAction(e -> new MainMenuView().show());
    }
}
