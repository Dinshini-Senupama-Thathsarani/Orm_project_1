package lk.orm.project01.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.PatientBO;
import lk.orm.project01.bo.TherapistBO;
import lk.orm.project01.bo.TherapistSessionBO;
import lk.orm.project01.bo.TherapyProgrammeBO;
import lk.orm.project01.dto.*;
import lk.orm.project01.exception.SchedulingConflictException;
import lk.orm.project01.exception.ValidationException;
import lk.orm.project01.tm.TherapySessionTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TherapySessionController implements Initializable {

    // ---- Form Fields ----
    @FXML private TextField        txtSessionId;
    @FXML private TextField        txtSessionDate;
    @FXML private TextField        txtSessionTime;
    @FXML private ComboBox<String> cmbPatient;
    @FXML private ComboBox<String> cmbTherapist;
    @FXML private ComboBox<String> cmbProgramme;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TextArea         txtNotes;
    @FXML private ComboBox<String> cmbFilterStatus;

    // ---- Table ----
    @FXML private TableView<TherapySessionTM>          tblSessions;
    @FXML private TableColumn<TherapySessionTM, String> colId;
    @FXML private TableColumn<TherapySessionTM, String> colDate;
    @FXML private TableColumn<TherapySessionTM, String> colTime;
    @FXML private TableColumn<TherapySessionTM, String> colPatient;
    @FXML private TableColumn<TherapySessionTM, String> colTherapist;
    @FXML private TableColumn<TherapySessionTM, String> colProgramme;
    @FXML private TableColumn<TherapySessionTM, String> colStatus;

    // ---- BOs ----
    private final TherapistSessionBO sessionBO =
        (TherapistSessionBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPIST_SESSION);
    private final PatientBO patientBO =
        (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private final TherapistBO therapistBO =
        (TherapistBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPIST);
    private final TherapyProgrammeBO programmeBO =
        (TherapyProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPY_PROGRAMME);

    private RegisterDTO currentUser;

    // Maps for ID lookup from display name
    private List<PatientDTO>          patientList;
    private List<TherapistDTO>        therapistList;
    private List<TherapyProgrammeDTO> programmeList;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Table column bindings
        colId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colTherapist.setCellValueFactory(new PropertyValueFactory<>("therapistName"));
        colProgramme.setCellValueFactory(new PropertyValueFactory<>("programmeName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Status options
        cmbStatus.setItems(FXCollections.observableArrayList(
            "SCHEDULED", "COMPLETED", "CANCELLED", "RESCHEDULED"
        ));
        cmbFilterStatus.setItems(FXCollections.observableArrayList(
            "ALL", "SCHEDULED", "COMPLETED", "CANCELLED", "RESCHEDULED"
        ));
        cmbFilterStatus.setValue("ALL");

        // Row click → populate form
        tblSessions.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) populateFormFromTM(sel); }
        );

        loadComboData();
        loadAllSessions();
        generateNextId();
    }

    @FXML
    void onBook(ActionEvent event) {
        try {
            TherapySessionDTO dto = collectFormData();
            sessionBO.bookSession(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Session booked successfully.");
            clearForm();
            loadAllSessions();
        } catch (SchedulingConflictException e) {
            showAlert(Alert.AlertType.WARNING, "Scheduling Conflict", e.getMessage());
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        try {
            TherapySessionDTO dto = collectFormData();
            sessionBO.updateSession(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Session updated.");
            clearForm();
            loadAllSessions();
        } catch (SchedulingConflictException e) {
            showAlert(Alert.AlertType.WARNING, "Scheduling Conflict", e.getMessage());
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        TherapySessionTM sel = tblSessions.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a session.");
            return;
        }
        Optional<ButtonType> res = showConfirm("Cancel Session",
            "Cancel session " + sel.getSessionId() + "?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                sessionBO.cancelSession(sel.getSessionId());
                showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Session cancelled.");
                clearForm();
                loadAllSessions();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML
    void onFilter(ActionEvent event) {
        String filter = cmbFilterStatus.getValue();
        try {
            List<TherapySessionDTO> list = "ALL".equals(filter)
                ? sessionBO.getAllSessions()
                : sessionBO.getSessionsByStatus(filter);
            populateTable(list);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Filter Error", e.getMessage());
        }
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
        loadAllSessions();
    }

    // ---- Helpers ----

    private void loadComboData() {
        try {
            patientList = patientBO.getAllPatients();
            for (PatientDTO p : patientList)
                cmbPatient.getItems().add(p.getPatientId() + " - " + p.getName());

            therapistList = therapistBO.getAllTherapists();
            for (TherapistDTO t : therapistList)
                cmbTherapist.getItems().add(t.getTherapistId() + " - " + t.getName());

            programmeList = programmeBO.getAllProgrammes();
            for (TherapyProgrammeDTO p : programmeList)
                cmbProgramme.getItems().add(p.getProgrammeId() + " - " + p.getName());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load combo data: " + e.getMessage());
        }
    }

    private void loadAllSessions() {
        try {
            populateTable(sessionBO.getAllSessions());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private void populateTable(List<TherapySessionDTO> sessions) {
        ObservableList<TherapySessionTM> tmList = FXCollections.observableArrayList();
        for (TherapySessionDTO s : sessions) {
            tmList.add(new TherapySessionTM(
                s.getSessionId(), s.getSessionDate(), s.getSessionTime(),
                s.getPatientName(), s.getTherapistName(),
                s.getProgrammeName(), s.getStatus()
            ));
        }
        tblSessions.setItems(tmList);
    }

    private void generateNextId() {
        try {
            txtSessionId.setText(sessionBO.generateNextSessionId());
        } catch (Exception e) {
            txtSessionId.setText("S001");
        }
    }

    private TherapySessionDTO collectFormData() {
        // Extract IDs from combo display strings (format: "ID - Name")
        String patientId   = extractId(cmbPatient.getValue());
        String therapistId = extractId(cmbTherapist.getValue());
        String programmeId = extractId(cmbProgramme.getValue());

        return new TherapySessionDTO(
            txtSessionId.getText().trim(),
            txtSessionDate.getText().trim(),
            txtSessionTime.getText().trim(),
            cmbStatus.getValue(),
            txtNotes.getText().trim(),
            patientId, "",
            therapistId, "",
            programmeId, ""
        );
    }

    private void populateFormFromTM(TherapySessionTM tm) {
        txtSessionId.setText(tm.getSessionId());
        txtSessionDate.setText(tm.getSessionDate());
        txtSessionTime.setText(tm.getSessionTime());
        cmbStatus.setValue(tm.getStatus());
        txtNotes.clear();
    }

    private String extractId(String comboValue) {
        if (comboValue == null || !comboValue.contains(" - ")) return "";
        return comboValue.split(" - ")[0].trim();
    }

    private void clearForm() {
        txtSessionDate.clear();
        txtSessionTime.clear();
        cmbPatient.setValue(null);
        cmbTherapist.setValue(null);
        cmbProgramme.setValue(null);
        cmbStatus.setValue(null);
        txtNotes.clear();
        tblSessions.getSelectionModel().clearSelection();
        generateNextId();
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
