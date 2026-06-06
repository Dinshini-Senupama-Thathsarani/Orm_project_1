package lk.orm.project01.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.TherapistBO;
import lk.orm.project01.bo.TherapyProgrammeBO;
import lk.orm.project01.dao.DAOFactory;
import lk.orm.project01.dao.TherapistDAO;
import lk.orm.project01.dao.TherapistProgrammeDAO;
import lk.orm.project01.dao.TherapyProgrammeDAO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.dto.TherapistDTO;
import lk.orm.project01.dto.TherapyProgrammeDTO;
import lk.orm.project01.entity.Therapist;
import lk.orm.project01.entity.TherapistProgramme;
import lk.orm.project01.entity.TherapyProgramme;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TherapistController implements Initializable {

    @FXML private TextField txtTherapistId;
    @FXML private TextField txtName;
    @FXML private TextField txtSpecialization;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbAvailability;
    @FXML private ComboBox<String> cmbProgramme;
    @FXML private TextField txtSearch;

    @FXML private TableView<TherapistDTO>          tblTherapists;
    @FXML private TableColumn<TherapistDTO, String> colId;
    @FXML private TableColumn<TherapistDTO, String> colName;
    @FXML private TableColumn<TherapistDTO, String> colSpec;
    @FXML private TableColumn<TherapistDTO, String> colPhone;
    @FXML private TableColumn<TherapistDTO, String> colEmail;
    @FXML private TableColumn<TherapistDTO, String> colAvail;

    private final TherapistBO therapistBO =
        (TherapistBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPIST);
    private final TherapyProgrammeBO programmeBO =
        (TherapyProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPY_PROGRAMME);
    private final TherapistProgrammeDAO therapistProgrammeDAO =
        (TherapistProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPIST_PROGRAMME);
    private final TherapistDAO therapistDAO =
        (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPIST);
    private final TherapyProgrammeDAO therapyProgrammeDAO =
        (TherapyProgrammeDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.THERAPY_PROGRAMME);

    private List<TherapyProgrammeDTO> programmeList;
    private RegisterDTO currentUser;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("therapistId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAvail.setCellValueFactory(new PropertyValueFactory<>("availability"));

        cmbAvailability.setItems(FXCollections.observableArrayList(
            "Monday-Friday", "Monday-Wednesday-Friday",
            "Tuesday-Thursday", "Weekends", "Full-Time"
        ));

        tblTherapists.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, selected) -> { if (selected != null) populateForm(selected); }
        );

        loadProgrammeCombo();
        loadAll();
        generateNextId();
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            TherapistDTO dto = collectFormData();
            therapistBO.saveTherapist(dto);

            String selectedProgramme = cmbProgramme != null ? cmbProgramme.getValue() : null;
            if (selectedProgramme != null && !selectedProgramme.isEmpty()) {
                String programmeId = extractProgrammeId(selectedProgramme);
                Therapist therapist = therapistDAO.findById(dto.getTherapistId());
                TherapyProgramme programme = therapyProgrammeDAO.findById(programmeId);
                if (therapist != null && programme != null) {
                    TherapistProgramme tp = new TherapistProgramme(
                        LocalDate.now().toString(),
                        "Lead Therapist",
                        therapist,
                        programme
                    );
                    therapistProgrammeDAO.save(tp);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Therapist saved successfully.");
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
    void onUpdate(ActionEvent event) {
        try {
            TherapistDTO dto = collectFormData();
            therapistBO.updateTherapist(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Therapist updated successfully.");
            clearForm();
            loadAll();
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        TherapistDTO selected = tblTherapists.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a therapist.");
            return;
        }
        Optional<ButtonType> result = showConfirm(
            "Confirm Delete", "Delete therapist '" + selected.getName() + "'?"
        );
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                therapistBO.deleteTherapist(selected.getTherapistId());
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Therapist deleted.");
                clearForm();
                loadAll();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
        loadAll();
    }

    @FXML
    void onSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) { loadAll(); return; }
        try {
            List<TherapistDTO> results = therapistBO.searchTherapistsByName(keyword);
            tblTherapists.setItems(FXCollections.observableArrayList(results));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Search Error", e.getMessage());
        }
    }

    private void loadProgrammeCombo() {
        try {
            programmeList = programmeBO.getAllProgrammes();
            ObservableList<String> items = FXCollections.observableArrayList();
            for (TherapyProgrammeDTO p : programmeList) {
                items.add(p.getProgrammeId() + " - " + p.getName());
            }
            if (cmbProgramme != null) cmbProgramme.setItems(items);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load programmes: " + e.getMessage());
        }
    }

    private void loadAll() {
        try {
            List<TherapistDTO> list = therapistBO.getAllTherapists();
            tblTherapists.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private void generateNextId() {
        try {
            txtTherapistId.setText(therapistBO.generateNextTherapistId());
        } catch (Exception e) {
            txtTherapistId.setText("T001");
        }
    }

    private TherapistDTO collectFormData() {
        return new TherapistDTO(
            txtTherapistId.getText().trim(),
            txtName.getText().trim(),
            txtSpecialization.getText().trim(),
            txtPhone.getText().trim(),
            txtEmail.getText().trim(),
            cmbAvailability.getValue()
        );
    }

    private void populateForm(TherapistDTO dto) {
        txtTherapistId.setText(dto.getTherapistId());
        txtName.setText(dto.getName());
        txtSpecialization.setText(dto.getSpecialization());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
        cmbAvailability.setValue(dto.getAvailability());
    }

    private void clearForm() {
        txtName.clear();
        txtSpecialization.clear();
        txtPhone.clear();
        txtEmail.clear();
        cmbAvailability.setValue(null);
        if (cmbProgramme != null) cmbProgramme.setValue(null);
        txtSearch.clear();
        tblTherapists.getSelectionModel().clearSelection();
        generateNextId();
    }

    private String extractProgrammeId(String comboValue) {
        if (comboValue == null || !comboValue.contains(" - ")) return "";
        return comboValue.split(" - ")[0].trim();
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
