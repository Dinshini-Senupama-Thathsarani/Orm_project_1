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
import lk.orm.project01.bo.PaymentBO;
import lk.orm.project01.dto.PatientDTO;
import lk.orm.project01.dto.PaymentDTO;
import lk.orm.project01.dto.RegisterDTO;
import lk.orm.project01.exception.PaymentException;
import lk.orm.project01.exception.ValidationException;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    // ---- Form Fields ----
    @FXML private TextField        txtPaymentId;
    @FXML private ComboBox<String> cmbPatient;
    @FXML private TextField        txtAmount;
    @FXML private TextField        txtDate;
    @FXML private ComboBox<String> cmbMethod;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private TextField        txtProgrammeId;
    @FXML private TextField        txtDescription;
    @FXML private Label            lblTotalRevenue;
    @FXML private ComboBox<String> cmbFilter;

    // ---- Table ----
    @FXML private TableView<PaymentDTO>          tblPayments;
    @FXML private TableColumn<PaymentDTO, String> colId;
    @FXML private TableColumn<PaymentDTO, String> colPatient;
    @FXML private TableColumn<PaymentDTO, Double> colAmount;
    @FXML private TableColumn<PaymentDTO, String> colDate;
    @FXML private TableColumn<PaymentDTO, String> colMethod;
    @FXML private TableColumn<PaymentDTO, String> colStatus;
    @FXML private TableColumn<PaymentDTO, String> colProgramme;

    private final PaymentBO paymentBO =
        (PaymentBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PAYMENT);
    private final PatientBO patientBO =
        (PatientBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PATIENT);

    private RegisterDTO currentUser;
    private List<PatientDTO> patientList;

    public void setCurrentUser(RegisterDTO user) {
        this.currentUser = user;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colProgramme.setCellValueFactory(new PropertyValueFactory<>("programmeId"));

        cmbMethod.setItems(FXCollections.observableArrayList("CASH", "CARD", "BANK_TRANSFER"));
        cmbStatus.setItems(FXCollections.observableArrayList("COMPLETED", "PENDING", "FAILED"));
        cmbFilter.setItems(FXCollections.observableArrayList("ALL", "PENDING", "COMPLETED"));
        cmbFilter.setValue("ALL");

        // Default date to today
        txtDate.setText(LocalDate.now().toString());

        tblPayments.getSelectionModel().selectedItemProperty().addListener(
            (obs, old, sel) -> { if (sel != null) populateForm(sel); }
        );

        loadPatientCombo();
        loadAll();
        generateNextId();
        refreshRevenue();
    }

    @FXML
    void onProcess(ActionEvent event) {
        try {
            PaymentDTO dto = collectFormData();
            paymentBO.processPayment(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Payment processed successfully.");
            clearForm();
            loadAll();
            refreshRevenue();
        } catch (PaymentException e) {
            showAlert(Alert.AlertType.WARNING, "Payment Error", e.getMessage());
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void onUpdate(ActionEvent event) {
        try {
            PaymentDTO dto = collectFormData();
            paymentBO.updatePayment(dto);
            showAlert(Alert.AlertType.INFORMATION, "Updated", "Payment updated.");
            clearForm();
            loadAll();
            refreshRevenue();
        } catch (PaymentException | ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    void onDelete(ActionEvent event) {
        PaymentDTO sel = tblPayments.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a payment.");
            return;
        }
        Optional<ButtonType> res = showConfirm("Delete Payment",
            "Delete payment " + sel.getPaymentId() + "?");
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                paymentBO.deletePayment(sel.getPaymentId());
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Payment deleted.");
                clearForm();
                loadAll();
                refreshRevenue();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            }
        }
    }

    @FXML
    void onFilter(ActionEvent event) {
        String filter = cmbFilter.getValue();
        try {
            List<PaymentDTO> list;
            switch (filter) {
                case "PENDING"   -> list = paymentBO.getPendingPayments();
                case "COMPLETED" -> list = paymentBO.getCompletedPayments();
                default          -> list = paymentBO.getAllPayments();
            }
            tblPayments.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Filter Error", e.getMessage());
        }
    }

    /** Generates a simple invoice alert for the selected payment. */
    @FXML
    void onGenerateInvoice(ActionEvent event) {
        PaymentDTO sel = tblPayments.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a payment.");
            return;
        }
        String invoice = String.format(
            "===== SERENITY THERAPY CENTER =====\n" +
            "INVOICE\n" +
            "-----------------------------------\n" +
            "Payment ID  : %s\n" +
            "Patient     : %s (%s)\n" +
            "Programme   : %s\n" +
            "Amount      : LKR %.2f\n" +
            "Date        : %s\n" +
            "Method      : %s\n" +
            "Status      : %s\n" +
            "Description : %s\n" +
            "===================================",
            sel.getPaymentId(), sel.getPatientName(), sel.getPatientId(),
            sel.getProgrammeId(), sel.getAmount(), sel.getPaymentDate(),
            sel.getPaymentMethod(), sel.getStatus(), sel.getDescription()
        );
        showAlert(Alert.AlertType.INFORMATION, "Invoice — " + sel.getPaymentId(), invoice);
    }

    @FXML
    void onClear(ActionEvent event) {
        clearForm();
        loadAll();
    }

    // ---- Helpers ----

    private void loadPatientCombo() {
        try {
            patientList = patientBO.getAllPatients();
            for (PatientDTO p : patientList)
                cmbPatient.getItems().add(p.getPatientId() + " - " + p.getName());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private void loadAll() {
        try {
            List<PaymentDTO> list = paymentBO.getAllPayments();
            tblPayments.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", e.getMessage());
        }
    }

    private void refreshRevenue() {
        try {
            double total = paymentBO.getTotalRevenue();
            lblTotalRevenue.setText(String.format("Total Revenue: LKR %.2f", total));
        } catch (Exception e) {
            lblTotalRevenue.setText("Total Revenue: N/A");
        }
    }

    private void generateNextId() {
        try {
            txtPaymentId.setText(paymentBO.generateNextPaymentId());
        } catch (Exception e) {
            txtPaymentId.setText("PAY001");
        }
    }

    private PaymentDTO collectFormData() {
        double amount = 0;
        try { amount = Double.parseDouble(txtAmount.getText().trim()); }
        catch (NumberFormatException ignored) {}

        String patientId = extractId(cmbPatient.getValue());

        return new PaymentDTO(
            txtPaymentId.getText().trim(),
            amount,
            txtDate.getText().trim(),
            cmbMethod.getValue(),
            cmbStatus.getValue(),
            txtProgrammeId.getText().trim(),
            txtDescription.getText().trim(),
            patientId, ""
        );
    }

    private void populateForm(PaymentDTO dto) {
        txtPaymentId.setText(dto.getPaymentId());
        txtAmount.setText(String.valueOf(dto.getAmount()));
        txtDate.setText(dto.getPaymentDate());
        cmbMethod.setValue(dto.getPaymentMethod());
        cmbStatus.setValue(dto.getStatus());
        txtProgrammeId.setText(dto.getProgrammeId());
        txtDescription.setText(dto.getDescription());
    }

    private String extractId(String comboValue) {
        if (comboValue == null || !comboValue.contains(" - ")) return "";
        return comboValue.split(" - ")[0].trim();
    }

    private void clearForm() {
        txtAmount.clear();
        txtDate.setText(LocalDate.now().toString());
        cmbPatient.setValue(null);
        cmbMethod.setValue(null);
        cmbStatus.setValue(null);
        txtProgrammeId.clear();
        txtDescription.clear();
        tblPayments.getSelectionModel().clearSelection();
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
