package lk.orm.project01.dto;


public class TherapySessionDTO {

    private String sessionId;
    private String sessionDate;
    private String sessionTime;
    private String status;
    private String notes;
    private String patientId;
    private String patientName;
    private String therapistId;
    private String therapistName;
    private String programmeId;
    private String programmeName;



    public TherapySessionDTO() {}

    public TherapySessionDTO(String sessionId, String sessionDate, String sessionTime,
                              String status, String notes,
                              String patientId, String patientName,
                              String therapistId, String therapistName,
                              String programmeId, String programmeName) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.status = status;
        this.notes = notes;
        this.patientId = patientId;
        this.patientName = patientName;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.programmeId = programmeId;
        this.programmeName = programmeName;
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

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

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
        return "TherapySessionDTO{sessionId='" + sessionId + "', date='" + sessionDate + "', status='" + status + "'}";
    }
}
