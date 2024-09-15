package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {

    private String id;

    @JsonProperty("employee_name")
    private String employeeName;

    @JsonProperty("employee_salary")
    private String salary;

    @JsonProperty("employee_age")
    private String age;

    @JsonProperty("profile_image")
    private String profilePicture;

    public Employee() {
    }

    public Employee(String id, String employeeName, String salary, String age, String profilePicture) {
        this.id = id;
        this.employeeName = employeeName;
        this.salary = salary;
        this.age = age;
        this.profilePicture = profilePicture;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
