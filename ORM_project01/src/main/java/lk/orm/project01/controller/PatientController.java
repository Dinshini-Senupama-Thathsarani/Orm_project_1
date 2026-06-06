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
import lk.orm.project01.bo.PatientProgrammeBO;
import lk.orm.project01.bo.PaymentBO;
import lk.orm.project01.bo.TherapyProgrammeBO;
import lk.orm.project01.dto.PatientDTO;
import lk.orm.project01.dto.PatientProgrammeDTO;
import lk.orm.project01.dto.PaymentDTO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.dto.TherapyProgrammeDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class PatientController implements Initializable {

    @FXML private TextField  txtPatientId;
    @FXML private TextField  txtName;
    @FXML private TextField  txtNic;
    @FXML private TextField  txtDob;
    @FXML private ComboBox<String> cmbGender;
    @FXML private TextField  txtAddress;
    @FXML private TextField  txtPhone;
    @FXML private TextField  txtEmail;
    @FXML private TextArea   txtMedicalHistory;
    @FXML private TextField  txtSearch;
    @FXML private ComboBox<String> cmbProgramme;
    @FXML private TextField  txtDownPayment;

    @FXML private TableView<PatientDTO>          tblPatients;
    @FXML private TableColumn<PatientDTO, String> colId;
    @FXML private TableColumn<PatientDTO, String> colName;
    @FXML private TableColumn<PatientDTO, String> colNic;
    @FXML private TableColumn<PatientDTO, String> colDob;
    @FXML private TableColumn<PatientDTO, String> colGender;
    @FXML private TableColumn<PatientDTO, String> colPhone;
    @FXML private TableColumn<PatientDTO, String> colEmail;

    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private final PatientBO patientBO =
        (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);
    private final TherapyProgrammeBO programmeBO =
        (TherapyProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPY_PROGRAMME);
    private final PatientProgrammeBO patientProgrammeBO =
        (PatientProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT_PROGRAMME);
    private final PaymentBO paymentBO =
        (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);

    private List<TherapyProgrammeDTO> programmeList;
    private RegisterDTO currentUser;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cmbGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        tblPatients.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, selected) -> {
                if (selected != null) populateForm(selected);
            }
        );

        loadProgrammeCombo();
        loadAllPatients();
        generateNextId();
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            PatientDTO dto = collectFormData();
            patientBO.savePatient(dto);

            String selectedProgramme = cmbProgramme.getValue();
            if (selectedProgramme != null && !selectedProgramme.isEmpty()) {
                String programmeId = extractProgrammeId(selectedProgramme);
                TherapyProgrammeDTO prog = getProgrammeById(programmeId);

                PatientProgrammeDTO ppDTO = new PatientProgrammeDTO(
                    null,
                    LocalDate.now().toString(),
                    "ACTIVE",
                    dto.getPatientId(),
                    dto.getName(),
                    programmeId,
                    prog != null ? prog.getName() : "",
                    prog != null ? prog.getFee() : 0.0
                );
                patientProgrammeBO.enrollPatient(ppDTO);

                String downPaymentText = txtDownPayment != null ? txtDownPayment.getText().trim() : "";
                if (!downPaymentText.isEmpty()) {
                    double downAmount = 0;
                    try { downAmount = Double.parseDouble(downPaymentText); } catch (NumberFormatException ignored) {}
                    if (downAmount > 0) {
                        String nextPayId = paymentBO.generateNextPaymentId();
                        PaymentDTO payDTO = new PaymentDTO(
                            nextPayId,
                            downAmount,
                            LocalDate.now().toString(),
                            "CASH",
                            "COMPLETED",
                            programmeId,
                            "Down payment for programme " + programmeId,
                            dto.getPatientId(),
                            dto.getName()
                        );
                        paymentBO.processPayment(payDTO);
                    }
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient saved successfully.");
            clearForm();
            loadAllPatients();
        } catch (DuplicateEntryException e) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Entry", e.getMessage());
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        try {
            PatientDTO dto = collectFormData();
            patientBO.updatePatient(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Patient updated successfully.");
            clearForm();
            loadAllPatients();
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        PatientDTO selected = tblPatients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a patient to delete.");
            return;
        }

        Optional<ButtonType> result = showConfirm(
            "Confirm Delete",
            "Delete patient '" + selected.getName() + "'?\nThis will also remove all related records."
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                patientBO.deletePatient(selected.getPatientId());
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Patient deleted successfully.");
                clearForm();
                loadAllPatients();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete patient: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
        loadAllPatients();
    }

    @FXML
    void onSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadAllPatients();
            return;
        }
        try {
            List<PatientDTO> results = patientBO.searchPatientsByName(keyword);
            tblPatients.setItems(FXCollections.observableArrayList(results));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Search Error", e.getMessage());
        }
    }

    private void loadProgrammeCombo() {
        try {
            programmeList = programmeBO.getAllProgrammes();
            ObservableList<String> items = FXCollections.observableArrayList();
            for (TherapyProgrammeDTO p : programmeList) {
                items.add(p.getProgrammeId() + " - " + p.getName() + " (LKR " + p.getFee() + ")");
            }
            cmbProgramme.setItems(items);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load programmes: " + e.getMessage());
        }
    }

    private void loadAllPatients() {
        try {
            List<PatientDTO> patients = patientBO.getAllPatients();
            ObservableList<PatientDTO> list = FXCollections.observableArrayList(patients);
            tblPatients.setItems(list);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load patients: " + e.getMessage());
        }
    }

    private void generateNextId() {
        try {
            txtPatientId.setText(patientBO.generateNextPatientId());
        } catch (Exception e) {
            txtPatientId.setText("P001");
        }
    }

    private PatientDTO collectFormData() {
        return new PatientDTO(
            txtPatientId.getText().trim(),
            txtName.getText().trim(),
            txtNic.getText().trim(),
            txtDob.getText().trim(),
            cmbGender.getValue(),
            txtAddress.getText().trim(),
            txtPhone.getText().trim(),
            txtEmail.getText().trim(),
            txtMedicalHistory.getText().trim()
        );
    }

    private void populateForm(PatientDTO dto) {
        txtPatientId.setText(dto.getPatientId());
        txtName.setText(dto.getName());
        txtNic.setText(dto.getNic());
        txtDob.setText(dto.getDob());
        cmbGender.setValue(dto.getGender());
        txtAddress.setText(dto.getAddress());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
        txtMedicalHistory.setText(dto.getMedicalHistory());
    }

    private void clearForm() {
        txtName.clear();
        txtNic.clear();
        txtDob.clear();
        cmbGender.setValue(null);
        txtAddress.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtMedicalHistory.clear();
        txtSearch.clear();
        cmbProgramme.setValue(null);
        if (txtDownPayment != null) txtDownPayment.clear();
        tblPatients.getSelectionModel().clearSelection();
        generateNextId();
    }

    private String extractProgrammeId(String comboValue) {
        if (comboValue == null || !comboValue.contains(" - ")) return "";
        return comboValue.split(" - ")[0].trim();
    }

    private TherapyProgrammeDTO getProgrammeById(String programmeId) {
        if (programmeList == null) return null;
        for (TherapyProgrammeDTO p : programmeList) {
            if (p.getProgrammeId().equals(programmeId)) return p;
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }
}
