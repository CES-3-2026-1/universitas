package co.edu.poli.ces3.universitas.dto;

public class Course {
    private String id;
    private String code;
    private String name;
    private String startDate;
    private String endDate;
    private int semester;

    public Course() {}

    public Course(String id, String code, String name, String startDate, String endDate, int semester) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.semester = semester;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
}
