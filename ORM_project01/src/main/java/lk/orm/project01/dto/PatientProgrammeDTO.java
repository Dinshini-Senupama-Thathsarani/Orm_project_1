package lk.orm.project01.dto;


public class PatientProgrammeDTO {

    private Long id;
    private String enrolledDate;
    private String status;
    private String patientId;
    private String patientName;
    private String programmeId;
    private String programmeName;
    private double programmeFee;



    public PatientProgrammeDTO() {}

    public PatientProgrammeDTO(Long id, String enrolledDate, String status,
                                String patientId, String patientName,
                                String programmeId, String programmeName,
                                double programmeFee) {
        this.id = id;
        this.enrolledDate = enrolledDate;
        this.status = status;
        this.patientId = patientId;
        this.patientName = patientName;
        this.programmeId = programmeId;
        this.programmeName = programmeName;
        this.programmeFee = programmeFee;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(String enrolledDate) { this.enrolledDate = enrolledDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getProgrammeName() { return programmeName; }
    public void setProgrammeName(String programmeName) { this.programmeName = programmeName; }

    public double getProgrammeFee() { return programmeFee; }
    public void setProgrammeFee(double programmeFee) { this.programmeFee = programmeFee; }

    @Override
    public String toString() {
        return "PatientProgrammeDTO{id=" + id + ", patientId='" + patientId + "', programmeId='" + programmeId + "'}";
    }
}
