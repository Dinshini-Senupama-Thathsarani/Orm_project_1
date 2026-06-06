package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import java.util.List;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "therapist_cache")
@Entity
@Table(name = "therapist")
public class Therapist {

    @Id
    @Column(name = "therapist_id", nullable = false, unique = true, length = 10)
    private String therapistId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "availability", length = 50)
    private String availability;

    // ---- Relationships ----

    /** One therapist → many therapy sessions */
    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TherapySession> therapySessions = new ArrayList<>();

    /** One therapist → many programme assignments */
    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TherapistProgramme> therapistProgrammes = new ArrayList<>();

    // ---- Constructors ----

    public Therapist() {}

    public Therapist(String therapistId, String name, String specialization,
                     String phone, String email, String availability) {
        this.therapistId = therapistId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.availability = availability;
    }

    // ---- Getters & Setters ----

    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public List<TherapySession> getTherapySessions() { return therapySessions; }
    public void setTherapySessions(List<TherapySession> therapySessions) { this.therapySessions = therapySessions; }

    public List<TherapistProgramme> getTherapistProgrammes() { return therapistProgrammes; }
    public void setTherapistProgrammes(List<TherapistProgramme> therapistProgrammes) { this.therapistProgrammes = therapistProgrammes; }

    @Override
    public String toString() {
        return "Therapist{therapistId='" + therapistId + "', name='" + name + "'}";
    }
}
