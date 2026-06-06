package lk.orm.project01.tm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class TherapySessionTM {

    private final StringProperty sessionId;
    private final StringProperty sessionDate;
    private final StringProperty sessionTime;
    private final StringProperty patientName;
    private final StringProperty therapistName;
    private final StringProperty programmeName;
    private final StringProperty status;

    public TherapySessionTM(String sessionId, String sessionDate, String sessionTime,
                             String patientName, String therapistName,
                             String programmeName, String status) {
        this.sessionId     = new SimpleStringProperty(sessionId);
        this.sessionDate   = new SimpleStringProperty(sessionDate);
        this.sessionTime   = new SimpleStringProperty(sessionTime);
        this.patientName   = new SimpleStringProperty(patientName);
        this.therapistName = new SimpleStringProperty(therapistName);
        this.programmeName = new SimpleStringProperty(programmeName);
        this.status        = new SimpleStringProperty(status);
    }

    // ---- Property Getters ----

    public StringProperty sessionIdProperty()     { return sessionId; }
    public StringProperty sessionDateProperty()   { return sessionDate; }
    public StringProperty sessionTimeProperty()   { return sessionTime; }
    public StringProperty patientNameProperty()   { return patientName; }
    public StringProperty therapistNameProperty() { return therapistName; }
    public StringProperty programmeNameProperty() { return programmeName; }
    public StringProperty statusProperty()        { return status; }

    // ---- Value Getters ----

    public String getSessionId()     { return sessionId.get(); }
    public String getSessionDate()   { return sessionDate.get(); }
    public String getSessionTime()   { return sessionTime.get(); }
    public String getPatientName()   { return patientName.get(); }
    public String getTherapistName() { return therapistName.get(); }
    public String getProgrammeName() { return programmeName.get(); }
    public String getStatus()        { return status.get(); }
}
