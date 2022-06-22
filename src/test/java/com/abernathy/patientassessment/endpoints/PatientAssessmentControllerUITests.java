package com.abernathy.patientassessment.endpoints;

import com.abernathy.patientassessment.domain.Patient;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.remote.HistoryRemote;
import com.abernathy.patientassessment.remote.PatientRemote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PatientAssessmentControllerUITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    HistoryRemote historyRemote;

    @MockBean
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
    public void patientAssessmentControllerGetViewAssessmentPage() throws Exception {

        //If our service works and asks the repo for patient with id 1, return our mock patient & notes
        when(patientRemote.getPatientById(1)).thenReturn(noRiskPatient);
        when(historyRemote.getHistoryForPatient(1)).thenReturn(noRiskPatientNotes);

        //Attempt to retrieve patient
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/assessment/view/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        //Verify:
        // - we get http response OK / 200
        // - we receive the correct html template
        // - the model contains the expected assessment result
        // - our mock patient & history apis were called once each with the correct patient ID
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        assertTrue(mvcResult.getModelAndView().getViewName().equals("assessment/view"));
        assertTrue(mvcResult.getModelAndView().getModel().get("patientAssessment").toString()
                .equals("Patient: No Risk (age 54) diabetes assessment is: None"));
        Mockito.verify(patientRemote, Mockito.times(1)).getPatientById(1);
        Mockito.verify(historyRemote, Mockito.times(1)).getHistoryForPatient(1);
    }

    @Test
    public void patientAssessmentControllerGetViewAssessmentPatientNotFound() throws Exception {

        //If our service works and asks the repo for patient with id 1, return no patient
        when(patientRemote.getPatientById(1)).thenReturn(null);
        when(historyRemote.getHistoryForPatient(1)).thenReturn(new ArrayList<>());

        //Attempt to retrieve patient
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/assessment/view/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println("ViewName: " + mvcResult.getModelAndView().getViewName());

        //Verify:
        // - we get http response OK / 200
        // - we receive the correct error html template
        // - our mock patient & history apis were called once each with the correct patient ID
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        assertTrue(mvcResult.getModelAndView().getViewName().equals("patnotfound"));
        Mockito.verify(patientRemote, Mockito.times(1)).getPatientById(1);
        Mockito.verify(historyRemote, Mockito.times(1)).getHistoryForPatient(1);
    }

    @Test
    public void patientAssessmentControllerGetViewAssessmentPatientHasNoNotes() throws Exception {

        //If our service works and asks the repo for patient with id 1, return patient but no notes
        when(patientRemote.getPatientById(1)).thenReturn(noRiskPatient);
        when(historyRemote.getHistoryForPatient(1)).thenReturn(new ArrayList<>());

        //Attempt to retrieve patient
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/assessment/view/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        System.out.println("ViewName: " + mvcResult.getModelAndView().getViewName());

        //Verify:
        // - we get http response OK / 200
        // - we receive the correct error html template
        // - our mock patient & history apis were called once each with the correct patient ID
        assertTrue(mvcResult.getResponse().getStatus() == 200);
        assertTrue(mvcResult.getModelAndView().getViewName().equals("patnonotes"));
        Mockito.verify(patientRemote, Mockito.times(1)).getPatientById(1);
        Mockito.verify(historyRemote, Mockito.times(1)).getHistoryForPatient(1);
    }

}
