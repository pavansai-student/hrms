package com.hrms.model;

public class Organization {

    private String name;
    private String address;
    private String pan;
    private String gst;
    private String department;
    private String email;
    private int employeeCount;

    public Organization(
            String name,
            String address,
            String pan,
            String gst,
            String department,
            String email,
            int employeeCount) {

        this.name = name;
        this.address = address;
        this.pan = pan;
        this.gst = gst;
        this.department = department;
        this.email = email;
        this.employeeCount = employeeCount;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPan() {
        return pan;
    }

    public String getGst() {
        return gst;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }
}
