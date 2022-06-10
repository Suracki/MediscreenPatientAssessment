package com.abernathy.patientassessment.remote;

import com.abernathy.patientassessment.domain.Patient;
import com.abernathy.patientassessment.domain.PatientNote;
import com.abernathy.patientassessment.remote.interfaces.HistoryRetro;
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

import java.util.List;

@Service
public class HistoryRemote {

    @Value("${docker.history.ip}")
    private String ip = "127.0.0.1";

    @Value("${docker.history.port}")
    private String port = "8181";

    private Logger logger = LoggerFactory.getLogger(HistoryRemote.class);

    private Gson gson = new GsonBuilder().setLenient().create();

    public List<PatientNote> getHistoryForPatient(int id) {
        logger.info("getHistoryForPatient called");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + ":" + port + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        HistoryRetro historyService = retrofit.create(HistoryRetro.class);

        Call<List<PatientNote>> callSync = historyService.getPatientHistory(id);

        try {
            Response<List<PatientNote>> response = callSync.execute();
            List<PatientNote> value = response.body();
            logger.debug("getHistoryForPatient external call completed");
            return value;
        } catch (Exception e) {
            logger.error("getHistoryForPatient external call failed: " + e);
            return null;
        }
    }

}
