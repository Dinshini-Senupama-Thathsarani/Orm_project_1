package lk.orm.project01.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.LoginBO;
import lk.orm.project01.bo.RegisterBO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.InvalidCredentialsException;
import lk.orm.project01.exception.ValidationException;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    // ---- Form Fields ----
    @FXML private TextField        txtUsername;
    @FXML private PasswordField    txtPassword;
    @FXML private PasswordField    txtConfirmPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private TextField        txtEmail;

    // ---- Change Password Section ----
    @FXML private TextField     txtCpUsername;
    @FXML private PasswordField txtOldPassword;
    @FXML private PasswordField txtNewPassword;

    // ---- Table ----
    @FXML private TableView<RegisterDTO>          tblUsers;
    @FXML private TableColumn<RegisterDTO, String> colUsername;
    @FXML private TableColumn<RegisterDTO, String> colRole;
    @FXML private TableColumn<RegisterDTO, String> colEmail;

    private final RegisterBO registerBO =
        (RegisterBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.REGISTER);
    private final LoginBO loginBO =
        (LoginBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.LOGIN);

    private RegisterDTO currentUser;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cmbRole.setItems(FXCollections.observableArrayList("ADMIN", "RECEPTIONIST"));

        tblUsers.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> {
                if (sel != null) {
                    txtUsername.setText(sel.getUsername());
                    cmbRole.setValue(sel.getRole());
                    txtEmail.setText(sel.getEmail());
                }
            }
        );

        loadAll();
    }

    @FXML
    void onRegister(ActionEvent event) {
        // Validate password confirmation
        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch", "Passwords do not match.");
            return;
        }

        try {
            RegisterDTO dto = new RegisterDTO(
                txtUsername.getText().trim(),
                txtPassword.getText(),
                cmbRole.getValue(),
                txtEmail.getText().trim()
            );
            registerBO.register(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully.");
            clearForm();
            loadAll();
        } catch (DuplicateEntryException e) {
            showAlert(Alert.AlertType.WARNING, "Duplicate", e.getMessage());
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        RegisterDTO sel = tblUsers.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user.");
            return;
        }
        // Prevent deleting own account
        if (currentUser != null && currentUser.getUsername().equals(sel.getUsername())) {
            showAlert(Alert.AlertType.WARNING, "Not Allowed", "You cannot delete your own account.");
            return;
        }
        Optional<ButtonType> res = showConfirm("Delete User",
            "Delete user '" + sel.getUsername() + "'?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                registerBO.delete(sel.getUsername());
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "User deleted.");
                clearForm();
                loadAll();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML
    void onChangePassword(ActionEvent event) {
        try {
            loginBO.changePassword(
                txtCpUsername.getText().trim(),
                txtOldPassword.getText(),
                txtNewPassword.getText()
            );
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
            txtCpUsername.clear();
            txtOldPassword.clear();
            txtNewPassword.clear();
        } catch (InvalidCredentialsException e) {
            showAlert(Alert.AlertType.WARNING, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
    }

    // ---- Helpers ----

    private void loadAll() {
        try {
            List<RegisterDTO> list = registerBO.getAllUsers();
            tblUsers.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private void clearForm() {
        txtUsername.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
        cmbRole.setValue(null);
        txtEmail.clear();
        tblUsers.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        a.showAndWait();
    }

    private Optional<ButtonType> showConfirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(msg);
        return a.showAndWait();
    }
}
