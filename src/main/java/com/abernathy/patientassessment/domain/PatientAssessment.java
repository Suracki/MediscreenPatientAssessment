package com.abernathy.patientassessment.domain;

import com.abernathy.patientassessment.domain.enums.Risk;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

public class PatientAssessment {

    Patient patient;
    private List<PatientNote> notes;
    private Risk risk;
    private int age;

    public PatientAssessment(Patient patient, List<PatientNote> notes) {
        this.patient = patient;
        this.notes = notes;
        age = getAge();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<PatientNote> getNotes() {
        return notes;
    }

    public void setNotes(List<PatientNote> notes) {
        this.notes = notes;
    }

    public Risk getRisk() {
        return risk;
    }

    public String riskString(){
        if (risk.equals(risk.BORDERLINE)) {
            return "Borderline";
        }
        else if (risk.equals(risk.INDANGER)) {
            return "In danger";
        }
        else if (risk.equals(risk.EARLYONSET)) {
            return "Early onset";
        }
        else {
            return "None";
        }
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public int getAge() {
        if (patient==null) {
            return 0;
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(patient.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), currentDate).getYears();
    }

    @Override
    public String toString() {
        return "Patient: " + patient.getGivenName() + " "
                + patient.getFamilyName() + " (age "
                + getAge() + ") diabetes assessment is: "
                + riskString();
    }

}
