package com.hrms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {

    private long id;
    private long organizationId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;
    private String designation;
    private LocalDate joiningDate;
    private BigDecimal salary;

    public Employee(
            long organizationId,
            String employeeCode,
            String firstName,
            String lastName,
            String email,
            String phone,
            String department,
            String designation,
            LocalDate joiningDate,
            BigDecimal salary) {

        this.organizationId = organizationId;
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.salary = salary;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
