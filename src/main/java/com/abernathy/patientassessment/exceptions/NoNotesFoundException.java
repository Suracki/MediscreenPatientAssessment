package com.abernathy.patientassessment.exceptions;

public class NoNotesFoundException extends Exception{

    private int patId;

    public NoNotesFoundException(int patId) {
        this.patId = patId;
    }

    public int getPatId() {
        return patId;
    }

}
