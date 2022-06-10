package com.abernathy.patientassessment.service;

import com.abernathy.patientassessment.domain.PatientAssessment;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.domain.TriggerCount;
import com.abernathy.patientassessment.exceptions.NoNotesFoundException;
import com.abernathy.patientassessment.exceptions.PatientNotFoundException;
import com.abernathy.patientassessment.remote.HistoryRemote;
import com.abernathy.patientassessment.remote.PatientRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatientAssessmentService {

    private final HistoryRemote historyRemote;
    private final PatientRemote patientRemote;

    private Logger logger = LoggerFactory.getLogger(PatientAssessmentService.class);

    public PatientAssessmentService(HistoryRemote historyRemote, PatientRemote patientRemote){
        this.historyRemote = historyRemote;
        this.patientRemote = patientRemote;
    }

    public PatientAssessment assessPatientRisk(int patId) throws PatientNotFoundException, NoNotesFoundException {
        //Create PatientAssessment object with Patient and their PatientNotes
        PatientAssessment patientAssessment = new PatientAssessment(patientRemote.getPatientById(patId),
                historyRemote.getHistoryForPatient(patId));
        //If we could not find a patient, or they have no notes, let caller know why we cannot generate an assessment
        if (patientAssessment.getPatient()==null) {
            throw new PatientNotFoundException(patId);
        }
        if (patientAssessment.getNotes().size()==0) {
            throw new NoNotesFoundException(patId);
        }

        // Parse notes & calculate risk
        int triggerCount = countTriggerTerms(patientAssessment.getNotes());


        return null;
    }

    private int countTriggerTerms(List<PatientNote> notes) {
        TriggerCount triggerCount = new TriggerCount();

        for (PatientNote patientNote : notes) {
            String note = patientNote.getNote().toLowerCase();
            if (note.contains("hemoglobin a1c")){
                triggerCount.addHemoglobin();
            }
            if (note.contains("microalbumin")){
                triggerCount.addMicroalbumin();
            }
            if (note.contains("body height")){
                triggerCount.addBodyHeight();
            }
            if (note.contains("body weight")){
                triggerCount.addBodyWeight();
            }
            if (note.contains("smoker")){
                triggerCount.addSmoker();
            }
            if (note.contains("abnormal")){
                triggerCount.addAbnormal();
            }
            if (note.contains("cholesterol")){
                triggerCount.addCholesterol();
            }
            if (note.contains("dizziness")){
                triggerCount.addDizziness();
            }
            if (note.contains("relapse")){
                triggerCount.addRelapse();
            }
            if (note.contains("reaction")){
                triggerCount.addReaction();
            }
            if (note.contains("antibodies")){
                triggerCount.addAntibodies();
            }
        }
        return triggerCount.getTriggerCount();
    }

}
