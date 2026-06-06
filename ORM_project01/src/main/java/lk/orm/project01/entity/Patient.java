package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE ,region ="patient_cache")
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @Column(name = "patient_id", nullable = false, unique = true, length = 10)
    private String patientId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "nic", unique = true, length = 12)
    private String nic;

    @Column(name = "dob", length = 20)
    private String dob;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    // ---- Relationships ----

    /** One patient → many therapy sessions */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TherapySession> therapySessions = new ArrayList<>();

    /** One patient → many programme enrollments */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PatientProgramme> patientProgrammes = new ArrayList<>();

    /** One patient → many payments */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    // ---- Constructors ----

    public Patient() {}

    public Patient(String patientId, String name, String nic, String dob,
                   String gender, String address, String phone, String email,
                   String medicalHistory) {
        this.patientId = patientId;
        this.name = name;
        this.nic = nic;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.medicalHistory = medicalHistory;
    }

    // ---- Getters & Setters ----

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public List<TherapySession> getTherapySessions() { return therapySessions; }
    public void setTherapySessions(List<TherapySession> therapySessions) { this.therapySessions = therapySessions; }

    public List<PatientProgramme> getPatientProgrammes() { return patientProgrammes; }
    public void setPatientProgrammes(List<PatientProgramme> patientProgrammes) { this.patientProgrammes = patientProgrammes; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }

    @Override
    public String toString() {
        return "Patient{patientId='" + patientId + "', name='" + name + "'}";
    }
}
