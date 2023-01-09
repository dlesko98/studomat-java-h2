package com.example.studomat.model;


import java.time.LocalDate;

public class Exam {

    private Long id;
    private LocalDate date;
    private Long subjectId;
    private String subjectName;

    public Exam(Long id, LocalDate date, Long subjectId, String subjectName) {
        this.id = id;
        this.date = date;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
