package com.abernathy.patientassessment.remote;

import com.abernathy.patientassessment.domain.Patient;
import com.abernathy.patientassessment.remote.interfaces.PatientRetro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Service
public class PatientRemote {

    @Value("${docker.patient.ip}")
    private String ip = "127.0.0.1";

    @Value("${docker.patient.port}")
    private String port = "8080";

    private Logger logger = LoggerFactory.getLogger(PatientRemote.class);

    private Gson gson = new GsonBuilder().setLenient().create();

    public Patient getPatient(int id) {
        logger.info("getUserByUsername called");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + ":" + port + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        PatientRetro patientService = retrofit.create(PatientRetro.class);

        Call<Patient> callSync = patientService.getPatient(id);

        try {
            Response<Patient> response = callSync.execute();
            Patient value = response.body();
            logger.debug("getUserByUsername external call completed");
            return value;
        } catch (Exception e) {
            logger.error("getUserByUsername external call failed: " + e);
            return null;
        }
    }

}
