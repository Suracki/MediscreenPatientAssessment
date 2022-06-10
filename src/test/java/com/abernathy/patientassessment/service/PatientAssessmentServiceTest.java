package com.abernathy.patientassessment.service;


import com.abernathy.patientassessment.domain.Patient;
import com.abernathy.patientassessment.domain.PatientAssessment;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.domain.enums.Risk;
import com.abernathy.patientassessment.exceptions.NoNotesFoundException;
import com.abernathy.patientassessment.exceptions.PatientNotFoundException;
import com.abernathy.patientassessment.remote.HistoryRemote;
import com.abernathy.patientassessment.remote.PatientRemote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PatientAssessmentServiceTest {

    @Mock
    HistoryRemote historyRemote;

    @Mock
    PatientRemote patientRemote;

    static Patient noRiskPatient;
    static List<PatientNote> noRiskPatientNotes;

    static Patient borderlinePatient;
    static List<PatientNote> borderlinePatientNotes;

    static Patient inDangerPatient;
    static List<PatientNote> inDangerPatientNotes;

    static Patient earlyOnsetPatient;
    static List<PatientNote> earlyOnsetPatientNotes;

    @BeforeAll
    public static void setUp() throws Exception {
        //Set up patient with NO RISK
        noRiskPatient = new Patient();
        noRiskPatient.setPatientId(1);
        noRiskPatient.setFamilyName("Risk");
        noRiskPatient.setGivenName("No");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        noRiskPatient.setDob(formatter.parse("1968-06-22"));
        noRiskPatient.setSex("M");
        noRiskPatient.setAddress("2 Test Street");
        noRiskPatient.setPhone("111-222-3333");
        //Set up notes for patient with NO RISK
        noRiskPatientNotes = new ArrayList<>();
        PatientNote noteNoRisk = new PatientNote();
        noteNoRisk.setId("ONE");
        noteNoRisk.setPatId(1);
        noteNoRisk.setNote("Patient has no symptoms");
        noRiskPatientNotes.add(noteNoRisk);

        //Set up patient with BORDERLINE RISK
        borderlinePatient = new Patient();
        borderlinePatient.setPatientId(1);
        borderlinePatient.setFamilyName("Line");
        borderlinePatient.setGivenName("Border");
        borderlinePatient.setDob(formatter.parse("1968-06-22"));
        borderlinePatient.setSex("M");
        borderlinePatient.setAddress("2 Test Street");
        borderlinePatient.setPhone("111-222-3333");
        //Set up notes for patient with BORDERLINE RISK
        borderlinePatientNotes = new ArrayList<>();
        PatientNote noteBorderline = new PatientNote();
        noteBorderline.setId("ONE");
        noteBorderline.setPatId(1);
        noteBorderline.setNote("Patient notes contain hemoglobin a1c and also smoker");
        borderlinePatientNotes.add(noteBorderline);

        //Set up patient with IN DANGER RISK
        inDangerPatient = new Patient();
        inDangerPatient.setPatientId(1);
        inDangerPatient.setFamilyName("Danger");
        inDangerPatient.setGivenName("In");
        inDangerPatient.setDob(formatter.parse("2000-06-22"));
        inDangerPatient.setSex("M");
        inDangerPatient.setAddress("2 Test Street");
        inDangerPatient.setPhone("111-222-3333");
        //Set up notes for patient with IN DANGER RISK
        inDangerPatientNotes = new ArrayList<>();
        PatientNote noteInDanger = new PatientNote();
        noteInDanger.setId("ONE");
        noteInDanger.setPatId(1);
        noteInDanger.setNote("Patient notes contain smoker body height abnormal and cholesterol");
        inDangerPatientNotes.add(noteInDanger);

        //Set up patient with EARLY ONSET RISK
        earlyOnsetPatient = new Patient();
        earlyOnsetPatient.setPatientId(1);
        earlyOnsetPatient.setFamilyName("Danger");
        earlyOnsetPatient.setGivenName("In");
        earlyOnsetPatient.setDob(formatter.parse("2000-06-22"));
        earlyOnsetPatient.setSex("M");
        earlyOnsetPatient.setAddress("2 Test Street");
        earlyOnsetPatient.setPhone("111-222-3333");
        //Set up notes for patient with EARLY ONSET RISK
        earlyOnsetPatientNotes = new ArrayList<>();
        PatientNote noteEarlyOnset = new PatientNote();
        noteEarlyOnset.setId("ONE");
        noteEarlyOnset.setPatId(1);
        noteEarlyOnset.setNote("Patient notes contain smoker body height abnormal cholesterol and relapse");
        earlyOnsetPatientNotes.add(noteEarlyOnset);
    }

    @Test
    public void patientAssessmentServiceCorrectlyAssessesNoRisk() throws Exception {

                PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(noRiskPatientNotes);
        when(patientRemote.getPatientById(1)).thenReturn(noRiskPatient);

        PatientAssessment assessment = patientAssessmentService.assessPatientRisk(1);

        System.out.println(assessment);
        assertTrue(assessment.getRisk().equals(Risk.NONE));
    }

    @Test
    public void patientAssessmentServiceCorrectlyAssessesBorderline() throws Exception {

        PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(borderlinePatientNotes);
        when(patientRemote.getPatientById(1)).thenReturn(borderlinePatient);

        PatientAssessment assessment = patientAssessmentService.assessPatientRisk(1);

        System.out.println(assessment);
        assertTrue(assessment.getRisk().equals(Risk.BORDERLINE));
    }

    @Test
    public void patientAssessmentServiceCorrectlyAssessesInDanger() throws Exception {

        PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(inDangerPatientNotes);
        when(patientRemote.getPatientById(1)).thenReturn(inDangerPatient);

        PatientAssessment assessment = patientAssessmentService.assessPatientRisk(1);

        System.out.println(assessment);
        assertTrue(assessment.getRisk().equals(Risk.INDANGER));
    }

    @Test
    public void patientAssessmentServiceCorrectlyAssessesEarlyOnset() throws Exception {

        PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(earlyOnsetPatientNotes);
        when(patientRemote.getPatientById(1)).thenReturn(earlyOnsetPatient);

        PatientAssessment assessment = patientAssessmentService.assessPatientRisk(1);

        System.out.println(assessment);
        assertTrue(assessment.getRisk().equals(Risk.EARLYONSET));
    }

    @Test
    public void patientAssessmentServiceThrowsErrorIfPatientNotFound()  {
        PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(null);
        when(patientRemote.getPatientById(1)).thenReturn(null);

        Exception exception = assertThrows(PatientNotFoundException.class, () -> {patientAssessmentService.assessPatientRisk(1);});

        assertTrue(exception.getClass().equals(PatientNotFoundException.class));
        assertTrue(((PatientNotFoundException) exception).getPatId() == 1);
    }

    @Test
    public void patientAssessmentServiceThrowsErrorIfPatientHasNoNotes()  {
        PatientAssessmentService patientAssessmentService = new PatientAssessmentService(historyRemote, patientRemote);

        when(historyRemote.getHistoryForPatient(1)).thenReturn(new ArrayList<>());
        when(patientRemote.getPatientById(1)).thenReturn(noRiskPatient);

        Exception exception = assertThrows(NoNotesFoundException.class, () -> {patientAssessmentService.assessPatientRisk(1);});

        assertTrue(exception.getClass().equals(NoNotesFoundException.class));
        assertTrue(((NoNotesFoundException) exception).getPatId() == 1);
    }

}
