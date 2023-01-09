package com.example.studomat.model;

public class Subject {

    private Long id;
    private String name;
    private Integer numberOfECTS;
    private Integer numberOfSemester;

    public Subject(Long id, String name, Integer numberOfECTS, Integer numberOfSemester) {
        this.id = id;
        this.name = name;
        this.numberOfECTS = numberOfECTS;
        this.numberOfSemester = numberOfSemester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfECTS() {
        return numberOfECTS;
    }

    public void setNumberOfECTS(Integer numberOfECTS) {
        this.numberOfECTS = numberOfECTS;
    }

    public Integer getNumberOfSemester() {
        return numberOfSemester;
    }

    public void setNumberOfSemester(Integer numberOfSemester) {
        this.numberOfSemester = numberOfSemester;
    }

    /*@Override
    public String toString() {
        return name + " iznosi " + numberOfECTS + " ECTS bodova, i polaže se u " + numberOfSemester + ". semestru!";
    }*/
    @Override
    public String toString() {
        return id.toString();
    }
}
