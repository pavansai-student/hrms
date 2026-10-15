package com.hrms.model;

import java.time.LocalDate;

public class Notice {

    private long id;
    private long organizationId;
    private String title;
    private String description;
    private String noticeType;
    private LocalDate startDate;
    private LocalDate endDate;

    public Notice(
            long organizationId,
            String title,
            String description,
            String noticeType,
            LocalDate startDate,
            LocalDate endDate) {

        this.organizationId = organizationId;
        this.title = title;
        this.description = description;
        this.noticeType = noticeType;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
