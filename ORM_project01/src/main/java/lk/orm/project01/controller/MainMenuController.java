package lk.orm.project01.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.tm.HelloApplication;

import java.io.IOException;


public class MainMenuController {

    @FXML private Label  lblWelcome;
    @FXML private Label  lblRole;
    @FXML private Button btnPatients;
    @FXML private Button btnTherapists;
    @FXML private Button btnProgrammes;
    @FXML private Button btnSessions;
    @FXML private Button btnPayments;
    @FXML private Button btnRegister;
    @FXML private AnchorPane contentPane;

    private RegisterDTO currentUser;

    /**
     * Called by LoginController after successful login.
     * Sets up the dashboard based on user role.
     *
     * @param user the logged-in user DTO
     */
    public void initData(RegisterDTO user) {
        this.currentUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername());
        lblRole.setText("Role: " + user.getRole());

        // Role-based access control
        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());

        // Therapists, Programmes, and Register management — Admin only
        btnTherapists.setDisable(!isAdmin);
        btnProgrammes.setDisable(!isAdmin);
        btnRegister.setDisable(!isAdmin);
    }



    @FXML
    void onPatients(ActionEvent event) {
        loadView("/lk/orm/project01/PatientManagement2.fxml");
    }

    @FXML
    void onTherapists(ActionEvent event) {
        loadView("/lk/orm/project01/TherapistManage.fxml");
    }

    @FXML
    void onProgrammes(ActionEvent event) {
        loadView("/lk/orm/project01/TherapyProgrammeManage.fxml");
    }

    @FXML
    void onSessions(ActionEvent event) {
        loadView("/lk/orm/project01/TherapySessionManage.fxml");
    }

    @FXML
    void onPayments(ActionEvent event) {
        loadView("/lk/orm/project01/PaymentManage.fxml");
    }

    @FXML
    void onRegister(ActionEvent event) {
        loadView("/lk/orm/project01/RegisterManage.fxml");
    }

    @FXML
    void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("/lk/orm/project01/login.fxml")
            );
            Scene scene = new Scene(loader.load(), 520, 400);
            Stage stage = (Stage) lblWelcome.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Serenity Mental Health Therapy Center");
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a sub-view FXML into the content pane.
     *
     * @param fxmlPath the resource path of the FXML to load
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource(fxmlPath)
            );
            AnchorPane view = loader.load();

            // Pass current user to sub-controllers that need it
            Object controller = loader.getController();
            if (controller instanceof PatientController pc) {
                pc.setCurrentUser(currentUser);
            } else if (controller instanceof TherapistController tc) {
                tc.setCurrentUser(currentUser);
            } else if (controller instanceof TherapyProgrammeController tpc) {
                tpc.setCurrentUser(currentUser);
            } else if (controller instanceof TherapySessionController tsc) {
                tsc.setCurrentUser(currentUser);
            } else if (controller instanceof PaymentController payC) {
                payC.setCurrentUser(currentUser);
            } else if (controller instanceof RegisterController rc) {
                rc.setCurrentUser(currentUser);
            }

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
