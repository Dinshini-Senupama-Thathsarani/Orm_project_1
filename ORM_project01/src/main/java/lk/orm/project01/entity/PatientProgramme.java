package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE ,region ="patient_programme")
@Entity
@Table(name = "patient_programme")
public class PatientProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Enrollment date */
    @Column(name = "enrolled_date", length = 20)
    private String enrolledDate;

    /** Status: ACTIVE, COMPLETED, DROPPED */
    @Column(name = "status", length = 20)
    private String status;



    /** Many enrollments → one patient */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Many enrollments → one therapy programme */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private TherapyProgramme therapyProgramme;

    // ---- Constructors ----

    public PatientProgramme() {}

    public PatientProgramme(String enrolledDate, String status,
                             Patient patient, TherapyProgramme therapyProgramme) {
        this.enrolledDate = enrolledDate;
        this.status = status;
        this.patient = patient;
        this.therapyProgramme = therapyProgramme;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(String enrolledDate) { this.enrolledDate = enrolledDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public TherapyProgramme getTherapyProgramme() { return therapyProgramme; }
    public void setTherapyProgramme(TherapyProgramme therapyProgramme) { this.therapyProgramme = therapyProgramme; }

    @Override
    public String toString() {
        return "PatientProgramme{id=" + id + ", status='" + status + "'}";
    }
}
