package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "theropyProgramme_cache")
@Entity
@Table(name = "therapy_programme")
public class TherapyProgramme {

    @Id
    @Column(name = "programme_id", nullable = false, unique = true, length = 10)
    private String programmeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration", length = 50)
    private String duration;

    @Column(name = "fee", nullable = false)
    private double fee;

    // ---- Relationships ----

    @OneToMany(mappedBy = "therapyProgramme", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PatientProgramme> patientProgrammes = new ArrayList<>();

    @OneToMany(mappedBy = "therapyProgramme", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TherapistProgramme> therapistProgrammes = new ArrayList<>();

    @OneToMany(mappedBy = "therapyProgramme", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TherapySession> therapySessions = new ArrayList<>();

    // ---- Constructors ----

    public TherapyProgramme() {}

    public TherapyProgramme(String programmeId, String name, String description,
                             String duration, double fee) {
        this.programmeId = programmeId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.fee = fee;
    }

    // ---- Getters & Setters ----

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public List<PatientProgramme> getPatientProgrammes() { return patientProgrammes; }
    public void setPatientProgrammes(List<PatientProgramme> patientProgrammes) { this.patientProgrammes = patientProgrammes; }

    public List<TherapistProgramme> getTherapistProgrammes() { return therapistProgrammes; }
    public void setTherapistProgrammes(List<TherapistProgramme> therapistProgrammes) { this.therapistProgrammes = therapistProgrammes; }

    public List<TherapySession> getTherapySessions() { return therapySessions; }
    public void setTherapySessions(List<TherapySession> therapySessions) { this.therapySessions = therapySessions; }

    @Override
    public String toString() {
        return "TherapyProgramme{programmeId='" + programmeId + "', name='" + name + "', fee=" + fee + "}";
    }
}
