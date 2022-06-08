package com.abernathy.patientassessment.remote.interfaces;

import com.abernathy.patientassessment.domain.PatientNote;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

@Service
public interface HistoryRetro {

    @GET("/patient/note/api/retro/getbypatient/{id}")
    public Call<List<PatientNote>> getPatientHistory(@Path("id") int id);

}
