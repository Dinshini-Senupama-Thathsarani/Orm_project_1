package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "theropySession_cache")
@Entity
@Table(name = "therapy_session")
public class TherapySession {

    @Id
    @Column(name = "session_id", nullable = false, unique = true, length = 10)
    private String sessionId;

    /** Session date (stored as String for simplicity, e.g. "2024-06-15") */
    @Column(name = "session_date", nullable = false, length = 20)
    private String sessionDate;

    /** Session time slot, e.g. "09:00 - 10:00" */
    @Column(name = "session_time", length = 30)
    private String sessionTime;

    /** Status: SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED */
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // ---- Relationships ----

    /** Many sessions → one patient */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    /** Many sessions → one therapist */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    /** Many sessions → one therapy programme */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private TherapyProgramme therapyProgramme;

    // ---- Constructors ----

    public TherapySession() {}

    public TherapySession(String sessionId, String sessionDate, String sessionTime,
                          String status, String notes,
                          Patient patient, Therapist therapist,
                          TherapyProgramme therapyProgramme) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.status = status;
        this.notes = notes;
        this.patient = patient;
        this.therapist = therapist;
        this.therapyProgramme = therapyProgramme;
    }

    // ---- Getters & Setters ----

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getSessionDate() { return sessionDate; }
    public void setSessionDate(String sessionDate) { this.sessionDate = sessionDate; }

    public String getSessionTime() { return sessionTime; }
    public void setSessionTime(String sessionTime) { this.sessionTime = sessionTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Therapist getTherapist() { return therapist; }
    public void setTherapist(Therapist therapist) { this.therapist = therapist; }

    public TherapyProgramme getTherapyProgramme() { return therapyProgramme; }
    public void setTherapyProgramme(TherapyProgramme therapyProgramme) { this.therapyProgramme = therapyProgramme; }

    @Override
    public String toString() {
        return "TherapySession{sessionId='" + sessionId + "', date='" + sessionDate + "', status='" + status + "'}";
    }
}
