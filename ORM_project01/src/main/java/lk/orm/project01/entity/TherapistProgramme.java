package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "therapistProgramme_cache")
@Entity
@Table(name = "therapist_programme")
public class TherapistProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Date assigned to the programme */
    @Column(name = "assigned_date", length = 20)
    private String assignedDate;

    /** Role in the programme, e.g. "Lead Therapist", "Co-Therapist" */
    @Column(name = "role_in_programme", length = 50)
    private String roleInProgramme;

    // ---- Relationships ----

    /** Many assignments → one therapist */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id", nullable = false)
    private Therapist therapist;

    /** Many assignments → one therapy programme */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programme_id", nullable = false)
    private TherapyProgramme therapyProgramme;

    // ---- Constructors ----

    public TherapistProgramme() {}

    public TherapistProgramme(String assignedDate, String roleInProgramme,
                               Therapist therapist, TherapyProgramme therapyProgramme) {
        this.assignedDate = assignedDate;
        this.roleInProgramme = roleInProgramme;
        this.therapist = therapist;
        this.therapyProgramme = therapyProgramme;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssignedDate() { return assignedDate; }
    public void setAssignedDate(String assignedDate) { this.assignedDate = assignedDate; }

    public String getRoleInProgramme() { return roleInProgramme; }
    public void setRoleInProgramme(String roleInProgramme) { this.roleInProgramme = roleInProgramme; }

    public Therapist getTherapist() { return therapist; }
    public void setTherapist(Therapist therapist) { this.therapist = therapist; }

    public TherapyProgramme getTherapyProgramme() { return therapyProgramme; }
    public void setTherapyProgramme(TherapyProgramme therapyProgramme) { this.therapyProgramme = therapyProgramme; }

    @Override
    public String toString() {
        return "TherapistProgramme{id=" + id + ", role='" + roleInProgramme + "'}";
    }
}
