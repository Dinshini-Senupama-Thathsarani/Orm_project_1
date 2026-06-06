package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region ="payment_cache")
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @Column(name = "payment_id", nullable = false, unique = true, length = 10)
    private String paymentId;

    @Column(name = "amount", nullable = false)
    private double amount;

    /** Payment date, e.g. "2024-06-15" */
    @Column(name = "payment_date", length = 20)
    private String paymentDate;

    /** Method: CASH, CARD, BANK_TRANSFER */
    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    /** Status: PENDING, COMPLETED, FAILED */
    @Column(name = "status", length = 20)
    private String status;

    /** Programme ID this payment is for */
    @Column(name = "programme_id", length = 10)
    private String programmeId;

    @Column(name = "description", length = 255)
    private String description;

    // ---- Relationships ----

    /** Many payments → one patient */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // ---- Constructors ----

    public Payment() {}

    public Payment(String paymentId, double amount, String paymentDate,
                   String paymentMethod, String status, String programmeId,
                   String description, Patient patient) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.programmeId = programmeId;
        this.description = description;
        this.patient = patient;
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

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    @Override
    public String toString() {
        return "Payment{paymentId='" + paymentId + "', amount=" + amount + ", status='" + status + "'}";
    }
}
