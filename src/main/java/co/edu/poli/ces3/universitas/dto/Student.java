package co.edu.poli.ces3.universitas.dto;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private String id;
    String document;
    private String firstName;
    private String lastName;
    private int age;
    private List<String[]> subjects;
    
    // Additional attributes
    private String email;
    private String phoneNumber;
    private String address;
    private double gpa; // Grade Point Average

    // Empty constructor (often needed for frameworks)
    public Student() {
        this.subjects = new ArrayList<>();
    }

    public Student(String lastName){
        this.lastName = lastName;
        this.subjects = new ArrayList<>();
    }

    public Student(Integer x){
        this.subjects = new ArrayList<>();
    }

    // Full constructor (overloaded)
    public Student(String id, String document, String firstName, String lastName, int age, 
                   String email, String phoneNumber, String address, double gpa) {
        this.id = id;
        this.document = document;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gpa = gpa;
        this.subjects = new ArrayList<>();
    }

    // Getters and Setters
    private String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<String[]> getSubjects() { return subjects; }
    public void setSubjects(List<String[]> subjects) { this.subjects = subjects; }

    public void addSubject(String code, String name) {
        this.subjects.add(new String[]{code, name});
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + "'" +
                ", document='" + document + "'"  +
                ", firstName='" + firstName + "'"  +
                ", lastName='" + lastName + "'"  +
                ", age=" + age +
                ", email='" + email + "'"  +
                ", gpa=" + gpa +
                '}';
    }

    // Main method to test the instance
    public static void main(String[] args) {
        // Using the empty constructor and setters
        Student student1 = new Student();
        student1.setId(UUID.randomUUID().toString());
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setDocument("12345678");
        student1.setAge(20);
        student1.setEmail("john.doe@example.com");
        student1.setGpa(3.8);

        // Using the full constructor
        Student student2 = new Student(
                UUID.randomUUID().toString(),
                "87654321",
                "Jane",
                "Smith",
                22,
                "jane.smith@example.com",
                "555-0199",
                "123 Maple St",
                4.0
        );

        System.out.println("Testing Student instances:");
        System.out.println("Student 1: " + student1);
        System.out.println("Student 2: " + student2);
    }
}
