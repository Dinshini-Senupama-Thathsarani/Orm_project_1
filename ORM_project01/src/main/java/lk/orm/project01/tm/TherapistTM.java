package lk.orm.project01.tm;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class TherapistTM {

    private final StringProperty therapistId;
    private final StringProperty name;
    private final StringProperty specialization;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty availability;

    public TherapistTM(String therapistId, String name, String specialization,
                       String phone, String email, String availability) {
        this.therapistId    = new SimpleStringProperty(therapistId);
        this.name           = new SimpleStringProperty(name);
        this.specialization = new SimpleStringProperty(specialization);
        this.phone          = new SimpleStringProperty(phone);
        this.email          = new SimpleStringProperty(email);
        this.availability   = new SimpleStringProperty(availability);
    }

    // ---- Property Getters (required by TableView) ----

    public StringProperty therapistIdProperty()    { return therapistId; }
    public StringProperty nameProperty()           { return name; }
    public StringProperty specializationProperty() { return specialization; }
    public StringProperty phoneProperty()          { return phone; }
    public StringProperty emailProperty()          { return email; }
    public StringProperty availabilityProperty()   { return availability; }

    // ---- Value Getters ----

    public String getTherapistId()    { return therapistId.get(); }
    public String getName()           { return name.get(); }
    public String getSpecialization() { return specialization.get(); }
    public String getPhone()          { return phone.get(); }
    public String getEmail()          { return email.get(); }
    public String getAvailability()   { return availability.get(); }
}
