package com.hrms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Salary {

    private long id;
    private long organizationId;
    private long employeeId;
    private LocalDate payMonth;
    private BigDecimal basicSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private String paymentStatus;

    public Salary(
            long organizationId,
            long employeeId,
            LocalDate payMonth,
            BigDecimal basicSalary,
            BigDecimal allowances,
            BigDecimal deductions,
            String paymentStatus) {

        this.organizationId = organizationId;
        this.employeeId = employeeId;
        this.payMonth = payMonth;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netSalary = basicSalary
                .add(allowances)
                .subtract(deductions);
        this.paymentStatus = paymentStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public LocalDate getPayMonth() {
        return payMonth;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public BigDecimal getAllowances() {
        return allowances;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
        calculateNetSalary();
    }

    public void setAllowances(BigDecimal allowances) {
        this.allowances = allowances;
        calculateNetSalary();
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
        calculateNetSalary();
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private void calculateNetSalary() {
        this.netSalary = basicSalary
                .add(allowances)
                .subtract(deductions);
    }
}
