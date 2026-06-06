package lk.orm.project01.dto;


public class PaymentDTO {

    private String paymentId;
    private double amount;
    private String paymentDate;
    private String paymentMethod;
    private String status;
    private String programmeId;
    private String description;
    private String patientId;
    private String patientName;


    public PaymentDTO() {}

    public PaymentDTO(String paymentId, double amount, String paymentDate,
                      String paymentMethod, String status, String programmeId,
                      String description, String patientId, String patientName) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.programmeId = programmeId;
        this.description = description;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    // ---- Getters & Setters ----

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    @Override
    public String toString() {
        return "PaymentDTO{paymentId='" + paymentId + "', amount=" + amount + ", status='" + status + "'}";
    }
}
