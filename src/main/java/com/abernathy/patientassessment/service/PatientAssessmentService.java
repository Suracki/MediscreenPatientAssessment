package com.abernathy.patientassessment.service;

import com.abernathy.patientassessment.domain.PatientAssessment;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.domain.enums.Risk;
import com.abernathy.patientassessment.exceptions.NoNotesFoundException;
import com.abernathy.patientassessment.exceptions.PatientNotFoundException;
import com.abernathy.patientassessment.remote.HistoryRemote;
import com.abernathy.patientassessment.remote.PatientRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
public class PatientAssessmentService {

    private final HistoryRemote historyRemote;
    private final PatientRemote patientRemote;

    @Value("${docker.patient.url}")
    private String urlPat;
    @Value("${docker.history.url}")
    private String urlNote;
    //Variable is initialized from application.properties in prod, variable is defined here for testing purposes
    //This is to ensure that trigger terms are always constant for comparison in tests
    @Value("#{'${trigger.terms.array}'.split(',')}")
    private List<String> triggerTerms = Arrays.asList(new String[]{"hemoglobin a1c","microalbumin","body height",
            "body weight","smoker","abnormal","cholesterol","dizziness","relapse","reaction","antibodies"});

    private Logger logger = LoggerFactory.getLogger(PatientAssessmentService.class);

    public PatientAssessmentService(HistoryRemote historyRemote, PatientRemote patientRemote){
        this.historyRemote = historyRemote;
        this.patientRemote = patientRemote;
    }

    /**
     * Method to generate assessment for Patient with ID provided via API
     * Returns NOT_FOUND if paitent doesnt exist, or patient has no notes and cannot be assessed
     *
     * @param patId
     * @return ResponseEntity of assessment and OK if successful
     */
    public ResponseEntity<String> patientRiskAssessmentApiRequest(int patId) {
        try {
            return new ResponseEntity<String>(assessPatientRisk(patId).toString(), new HttpHeaders(), HttpStatus.OK);
        }
        catch (PatientNotFoundException e) {
            return new ResponseEntity<String>("Patient " + patId + " not found.", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        catch (NoNotesFoundException f) {
            return new ResponseEntity<String>("Patient " + patId + " has no notes.", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Method to generate assessment for Patient with ID provided via front end UI
     * Generates assessment and returns appropriate page
     * - view assessment page if successful
     * - patnotfound error page if patient not found
     * - patnonotes error page if patient exists but has no notes
     *
     * @param id
     * @param model
     * @return ResponseEntity of assessment and OK if successful
     */
    public String patientRiskAssessmentWebRequest(int id, Model model) {
        try {
            PatientAssessment patientAssessment = assessPatientRisk(id);
            model.addAttribute("patientAssessment", patientAssessment);
            model.addAttribute("urlPat", urlPat);
            model.addAttribute("urlNote", urlNote);
            return "assessment/view";
        }
        catch (PatientNotFoundException e) {
            logger.debug("patient not found with ID " + id);
            model.addAttribute("patientId", id);
            model.addAttribute("urlPat", urlPat);
            model.addAttribute("urlNote", urlNote);
            return "patnotfound";
        }
        catch (NoNotesFoundException f) {
            logger.debug("no notes found for patient with ID " + id);
            model.addAttribute("patientId", id);
            model.addAttribute("urlPat", urlPat);
            model.addAttribute("urlNote", urlNote);
            return "patnonotes";
        }

    }

    /**
     * Method to generate assessment for Patient with provided ID
     *
     * @param patId
     * @throws PatientNotFoundException if Patient not found
     * @throws NoNotesFoundException if Patient has no notes or notes could not be loaded
     * @return PatientAssessment object containing details of patient, notes, and assessment
     */
    public PatientAssessment assessPatientRisk(int patId) throws PatientNotFoundException, NoNotesFoundException {
        logger.info("assessPatientRisk called for Patient ID: " + patId);
        //Create PatientAssessment object with Patient and their PatientNotes
        PatientAssessment patientAssessment = new PatientAssessment(patientRemote.getPatientById(patId),
                historyRemote.getHistoryForPatient(patId));
        //If we could not find a patient, or they have no notes, let caller know why we cannot generate an assessment
        if (patientAssessment.getPatient()==null) {
            throw new PatientNotFoundException(patId);
        }
        if (patientAssessment.getNotes() == null || patientAssessment.getNotes().size()==0) {
            throw new NoNotesFoundException(patId);
        }
        logger.debug("Patient located. Patient has " + patientAssessment.getNotes().size() + " notes.");

        // Parse notes & get number of trigger terms
        int triggerCount = countTriggers(patientAssessment.getNotes());

        // Set risk value of assessment
        calculateRisk(patientAssessment, triggerCount);

        logger.debug("Risk calculated. Risk value: " + patientAssessment.getRisk());
        logger.debug("Age: " + patientAssessment.getAge() + " Gender: " + patientAssessment.getPatient().getSex() +
                " Trigger Count: " + triggerCount);

        return patientAssessment;
    }

    // Calculate patient's risk value
    private void calculateRisk(PatientAssessment patientAssessment, int triggerCount) {

        patientAssessment.setRisk(Risk.NONE);

        if (triggerCount >= 2 && patientAssessment.getAge() > 29) {
            patientAssessment.setRisk(Risk.BORDERLINE);
        }
        if (triggerCount >= 3 && patientAssessment.getAge() < 30 && patientAssessment.getPatient().getSex().equals("M")) {
            patientAssessment.setRisk(Risk.INDANGER);
        }
        if (triggerCount >= 4 && patientAssessment.getAge() < 30 && patientAssessment.getPatient().getSex().equals("F")) {
            patientAssessment.setRisk(Risk.INDANGER);
        }
        if (triggerCount >= 5 && patientAssessment.getAge() < 30 && patientAssessment.getPatient().getSex().equals("M")) {
            patientAssessment.setRisk(Risk.EARLYONSET);
        }
        if (triggerCount >= 7 && patientAssessment.getAge() < 30 && patientAssessment.getPatient().getSex().equals("F")) {
            patientAssessment.setRisk(Risk.EARLYONSET);
        }
        if (triggerCount >= 8 && patientAssessment.getAge() > 29) {
            patientAssessment.setRisk(Risk.EARLYONSET);
        }

    }

    //Count the number of trigger terms that appear in patient's notes
    private int countTriggers(List<PatientNote> notes) {

        // Iterate through all note objects & join note text together
        String noteString = "";
        for (PatientNote patientNote : notes) {
            noteString = noteString + " " + patientNote.getNote();
        }
        String finalNoteString = noteString;

        List<String> termsInNotes = new ArrayList<>();

        // Iterate through terms, if it is found in the notes add it to list
        triggerTerms.forEach(term -> {
            if (finalNoteString.toLowerCase().contains(term.toLowerCase())) {
                termsInNotes.add(term);
            }
        });

        return termsInNotes.size();
    }
}
