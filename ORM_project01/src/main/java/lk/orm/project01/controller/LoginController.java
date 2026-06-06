package lk.orm.project01.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.LoginBO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.exception.InvalidCredentialsException;
import lk.orm.project01.tm.HelloApplication;

import java.io.IOException;


public class LoginController {

    @FXML private TextField     txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField     txtPasswordVisible;
    @FXML private CheckBox      chkShowPassword;
    @FXML private Button        btnLogin;
    @FXML private Label         lblError;


    private final LoginBO loginBO =
        (LoginBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.LOGIN);


    @FXML
    void onTogglePassword(ActionEvent event) {
        if (chkShowPassword.isSelected()) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
        }
    }


    @FXML
    void onLogin(ActionEvent event) {
        lblError.setText("");

        String username = txtUsername.getText().trim();
        String password = chkShowPassword.isSelected()
            ? txtPasswordVisible.getText()
            : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please enter username and password.");
            return;
        }

        try {
            RegisterDTO user = loginBO.login(username, password);
            navigateToMainMenu(user);
        } catch (InvalidCredentialsException e) {
            lblError.setText(e.getMessage());
        } catch (Exception e) {
            lblError.setText("System error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void navigateToMainMenu(RegisterDTO user) {
        try {
            FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/lk/orm/project01/owner_main-menu-view.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);

            MainMenuController controller = loader.getController();
            controller.initData(user);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Serenity Mental Health Therapy Center — Dashboard");
            stage.setResizable(true);
            stage.centerOnScreen();
        } catch (IOException e) {
            lblError.setText("Failed to load main menu.");
            e.printStackTrace();
        }
    }
}
