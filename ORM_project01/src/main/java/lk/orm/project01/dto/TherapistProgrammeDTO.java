package lk.orm.project01.dto;


public class TherapistProgrammeDTO {

    private Long id;
    private String assignedDate;
    private String roleInProgramme;
    private String therapistId;
    private String therapistName;
    private String programmeId;
    private String programmeName;


    public TherapistProgrammeDTO() {}

    public TherapistProgrammeDTO(Long id, String assignedDate, String roleInProgramme,
                                  String therapistId, String therapistName,
                                  String programmeId, String programmeName) {
        this.id = id;
        this.assignedDate = assignedDate;
        this.roleInProgramme = roleInProgramme;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.programmeId = programmeId;
        this.programmeName = programmeName;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAssignedDate() { return assignedDate; }
    public void setAssignedDate(String assignedDate) { this.assignedDate = assignedDate; }

    public String getRoleInProgramme() { return roleInProgramme; }
    public void setRoleInProgramme(String roleInProgramme) { this.roleInProgramme = roleInProgramme; }

    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }

    public String getTherapistName() { return therapistName; }
    public void setTherapistName(String therapistName) { this.therapistName = therapistName; }

    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getProgrammeName() { return programmeName; }
    public void setProgrammeName(String programmeName) { this.programmeName = programmeName; }

    @Override
    public String toString() {
        return "TherapistProgrammeDTO{id=" + id + ", therapistId='" + therapistId + "', programmeId='" + programmeId + "'}";
    }
}
