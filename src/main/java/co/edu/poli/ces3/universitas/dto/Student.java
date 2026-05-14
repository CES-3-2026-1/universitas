package co.edu.poli.ces3.universitas.dto;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String[]> subjects;

    public Student() {
        super();
        this.subjects = new ArrayList<>();
        this.setUserType("STUDENT");
    }

    public Student(String id, String document, String firstName, String lastName, int age, String email, 
                   String phoneNumber, String address, double gpa) {
        super(id, document, firstName, lastName, age, email, phoneNumber, address, gpa, "STUDENT");
        this.subjects = new ArrayList<>();
    }

    public List<String[]> getSubjects() { return subjects; }
    public void setSubjects(List<String[]> subjects) { this.subjects = subjects; }

    public void addSubject(String code, String name) {
        this.subjects.add(new String[]{code, name});
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + getId() + "'" +
                ", document='" + getDocument() + "'"  +
                ", firstName='" + getFirstName() + "'"  +
                ", lastName='" + getLastName() + "'"  +
                ", age=" + getAge() +
                ", email='" + getEmail() + "'"  +
                ", gpa=" + getGpa() +
                '}';
    }
}
