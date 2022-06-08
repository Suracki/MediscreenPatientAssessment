package com.abernathy.patientassessment.exceptions;

public class PatientNotFoundException extends Exception{

    private int patId;

    public PatientNotFoundException(int patId) {
        this.patId = patId;
    }

    public int getPatId() {
        return patId;
    }
}
