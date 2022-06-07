package com.abernathy.patientassessment.retrofit;

import com.abernathy.patientassessment.domain.Patient;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.remote.HistoryRemote;
import com.abernathy.patientassessment.remote.PatientRemote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RetrofitIT {

    @Test
    public void patientRemoteCanGetPatientDetails() {

        PatientRemote patientRemote = new PatientRemote();
        Patient patient = patientRemote.getPatientById(1);

        System.out.println("Retrieved patient: " + patient);
        assertTrue(patient != null);

    }

    @Test
    public void historyRemoteCanGetPatientHistory() {

        HistoryRemote historyRemote = new HistoryRemote();
        List<PatientNote> notes = historyRemote.getHistoryForPatient(1);

        System.out.println("Retrieved patient history: " + notes);
        assertTrue(notes.size() > 0);

    }

}
