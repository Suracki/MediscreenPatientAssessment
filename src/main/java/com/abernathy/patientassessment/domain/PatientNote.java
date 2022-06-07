package com.abernathy.patientassessment.domain;

public class PatientNote {

    private String patientNoteId;
    private String patId;
    private String note;

    public String getPatientNoteId() {
        return patientNoteId;
    }

    public void setPatientNoteId(String patientNoteId) {
        this.patientNoteId = patientNoteId;
    }

    public String getId() {
        return patientNoteId;
    }

    public void setId(String patientNoteId) {
        this.patientNoteId = patientNoteId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "{\"patientNote\":{\"patientNoteId\": \"" + patientNoteId +
                "\", \"patId\": \"" + patId +
                "\", \"note\": \"" + note +"\"}}";
    }
}
