package lk.orm.project01.dto;


public class TherapyProgrammeDTO {

    private String programmeId;
    private String name;
    private String description;
    private String duration;
    private double fee;



    public TherapyProgrammeDTO() {}

    public TherapyProgrammeDTO(String programmeId, String name, String description,
                                String duration, double fee) {
        this.programmeId = programmeId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.fee = fee;
    }



    public String getProgrammeId() { return programmeId; }
    public void setProgrammeId(String programmeId) { this.programmeId = programmeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    @Override
    public String toString() {
        return "TherapyProgrammeDTO{programmeId='" + programmeId + "', name='" + name + "', fee=" + fee + "}";
    }
}
