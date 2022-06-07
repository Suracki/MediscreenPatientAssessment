package com.abernathy.patientassessment.remote.interfaces;

import com.abernathy.patientassessment.domain.Patient;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Service
public interface PatientRetro {

    @GET("/patient/api/get/{id}")
    public Call<Patient> getPatient(@Path("id") int id);

}
