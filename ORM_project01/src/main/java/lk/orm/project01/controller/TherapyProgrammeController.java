package lk.orm.project01.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.orm.project01.bo.BOFactory;
import lk.orm.project01.bo.TherapyProgrammeBO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.dto.TherapyProgrammeDTO;
import lk.orm.project01.exception.DuplicateEntryException;
import lk.orm.project01.exception.ValidationException;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class TherapyProgrammeController implements Initializable {

    @FXML private TextField  txtProgrammeId;
    @FXML private TextField  txtName;
    @FXML private TextArea   txtDescription;
    @FXML private TextField  txtDuration;
    @FXML private TextField  txtFee;

    @FXML private TableView<TherapyProgrammeDTO>          tblProgrammes;
    @FXML private TableColumn<TherapyProgrammeDTO, String> colId;
    @FXML private TableColumn<TherapyProgrammeDTO, String> colName;
    @FXML private TableColumn<TherapyProgrammeDTO, String> colDuration;
    @FXML private TableColumn<TherapyProgrammeDTO, Double> colFee;

    private final TherapyProgrammeBO programmeBO =
        (TherapyProgrammeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.THERAPY_PROGRAMME);

    private RegisterDTO currentUser;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("programmeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        tblProgrammes.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) populateForm(sel); }
        );

        loadAll();
    }

    @FXML
    void onSave(ActionEvent event) {
        try {
            TherapyProgrammeDTO dto = collectFormData();
            programmeBO.saveProgramme(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Programme saved successfully.");
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
            TherapyProgrammeDTO dto = collectFormData();
            programmeBO.updateProgramme(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Programme updated.");
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
        TherapyProgrammeDTO sel = tblProgrammes.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a programme.");
            return;
        }
        Optional<ButtonType> res = showConfirm("Confirm Delete",
            "Delete programme '" + sel.getName() + "'?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                programmeBO.deleteProgramme(sel.getProgrammeId());
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Programme deleted.");
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

    // ---- Helpers ----

    private void loadAll() {
        try {
            List<TherapyProgrammeDTO> list = programmeBO.getAllProgrammes();
            tblProgrammes.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private TherapyProgrammeDTO collectFormData() {
        double fee = 0;
        try { fee = Double.parseDouble(txtFee.getText().trim()); }
        catch (NumberFormatException ignored) {}

        return new TherapyProgrammeDTO(
            txtProgrammeId.getText().trim(),
            txtName.getText().trim(),
            txtDescription.getText().trim(),
            txtDuration.getText().trim(),
            fee
        );
    }

    private void populateForm(TherapyProgrammeDTO dto) {
        txtProgrammeId.setText(dto.getProgrammeId());
        txtName.setText(dto.getName());
        txtDescription.setText(dto.getDescription());
        txtDuration.setText(dto.getDuration());
        txtFee.setText(String.valueOf(dto.getFee()));
    }

    private void clearForm() {
        txtProgrammeId.clear();
        txtName.clear();
        txtDescription.clear();
        txtDuration.clear();
        txtFee.clear();
        tblProgrammes.getSelectionModel().clearSelection();
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
