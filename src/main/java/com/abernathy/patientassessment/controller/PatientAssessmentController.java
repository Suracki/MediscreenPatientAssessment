package com.abernathy.patientassessment.controller;

import com.abernathy.patientassessment.service.PatientAssessmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PatientAssessmentController {

    @Autowired
    PatientAssessmentService patientAssessmentService;

    private static final Logger logger = LogManager.getLogger(PatientAssessmentController.class);


    //Endpoints for serving front end
    /**
     * Mapping for GET
     *
     * Serves view patient assessment page for Mediscreen app
     *
     * @param id patient id
     * @param model Model
     * @return view patient page
     */
    @GetMapping("/assessment/view/{id}")
    public String viewPatientAssessment(@PathVariable("id") Integer id, Model model) {
        logger.info("User connected to /assessment/view/ endpoint with id " + id);
        return patientAssessmentService.patientRiskAssessmentWebRequest(id, model);
    }

    //Endpoints for serving REST API
    /**
     * Mapping for GET
     *
     * Takes a Patient's ID, returns that Patient's assessment
     *
     * Returns:
     * HttpStatus.NOT_FOUND if patient cannot be found with provided ID, or patient has no notes
     * String & HttpStatus.OK if successful
     *
     * @param id
     * @return Assessment String & HttpStatus.OK if successful
     */
    @GetMapping("/assessment/api/get/{id}")
    public ResponseEntity<String> getPatientAssessment(@PathVariable("id") int id) {
        logger.info("User connected to /assessment/api/get/ endpoint with id " + id);
        return patientAssessmentService.patientRiskAssessmentApiRequest(id);
    }

}
