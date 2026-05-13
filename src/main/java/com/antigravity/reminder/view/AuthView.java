package com.antigravity.reminder.view;

import com.antigravity.reminder.viewmodel.AuthViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AuthView {
    private final AuthViewModel viewModel = new AuthViewModel();

    public void showLogin() {
        StackPane container = new StackPane();
        container.setPadding(new Insets(50));

        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxSize(400, 450);
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Вхід");
        titleLabel.getStyleClass().add("title-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Логін або Email");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");
        passwordField.setMaxWidth(300);

        Button loginButton = new Button("Увійти");
        loginButton.getStyleClass().add("button-primary");
        loginButton.setMaxWidth(300);
        loginButton.setMinHeight(45);

        Hyperlink registerLink = new Hyperlink("Немає акаунту? Зареєструйтеся тут");
        registerLink.setOnAction(e -> showRegister());

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #ef4444;");

        loginButton.setOnAction(
                e -> {
                    if (viewModel.login(usernameField.getText(), passwordField.getText())) {
                        new MainMenuView().show();
                    } else {
                        messageLabel.setText("Невірні облікові дані!");
                    }
                });

        card.getChildren()
                .addAll(
                        titleLabel,
                        usernameField,
                        passwordField,
                        loginButton,
                        registerLink,
                        messageLabel);
        container.getChildren().add(card);
        ViewManager.showView("Вхід", container);
    }

    public void showRegister() {
        StackPane container = new StackPane();
        container.setPadding(new Insets(50));

        VBox card = new VBox(15);
        card.getStyleClass().add("card");
        card.setMaxSize(400, 550);
        card.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Реєстрація");
        titleLabel.getStyleClass().add("title-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Логін");
        usernameField.setMaxWidth(300);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");
        passwordField.setMaxWidth(300);

        TextField timezoneField = new TextField("UTC");
        timezoneField.setPromptText("Часовий пояс (наприклад, UTC)");
        timezoneField.setMaxWidth(300);

        Button registerButton = new Button("Зареєструватися");
        registerButton.getStyleClass().add("button-primary");
        registerButton.setMaxWidth(300);
        registerButton.setMinHeight(45);

        Hyperlink loginLink = new Hyperlink("Вже маєте акаунт? Увійдіть тут");
        loginLink.setOnAction(e -> showLogin());

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #ef4444;");

        registerButton.setOnAction(
                e -> {
                    try {
                        viewModel.register(
                                usernameField.getText(),
                                emailField.getText(),
                                passwordField.getText(),
                                timezoneField.getText());
                        Alert alert =
                                new Alert(
                                        Alert.AlertType.INFORMATION,
                                        "Реєстрація успішна!",
                                        ButtonType.OK);
                        alert.showAndWait();
                        showLogin();
                    } catch (Exception ex) {
                        messageLabel.setText("Помилка реєстрації: " + ex.getMessage());
                    }
                });

        card.getChildren()
                .addAll(
                        titleLabel,
                        usernameField,
                        emailField,
                        passwordField,
                        timezoneField,
                        registerButton,
                        loginLink,
                        messageLabel);
        container.getChildren().add(card);
        ViewManager.showView("Реєстрація", container);
    }
}
