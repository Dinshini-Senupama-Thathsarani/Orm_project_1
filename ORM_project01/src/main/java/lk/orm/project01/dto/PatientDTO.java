package lk.orm.project01.dto;


public class PatientDTO {

    private String patientId;
    private String name;
    private String nic;
    private String dob;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String medicalHistory;



    public PatientDTO() {}

    public PatientDTO(String patientId, String name, String nic, String dob,
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

    @Override
    public String toString() {
        return "PatientDTO{patientId='" + patientId + "', name='" + name + "'}";
    }
}
