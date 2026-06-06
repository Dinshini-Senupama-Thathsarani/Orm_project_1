package lk.orm.project01.dto;


public class TherapistDTO {

    private String therapistId;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String availability;



    public TherapistDTO() {}

    public TherapistDTO(String therapistId, String name, String specialization,
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

    @Override
    public String toString() {
        return "TherapistDTO{therapistId='" + therapistId + "', name='" + name + "'}";
    }
}
