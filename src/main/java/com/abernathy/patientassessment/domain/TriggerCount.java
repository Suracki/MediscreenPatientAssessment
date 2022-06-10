package com.abernathy.patientassessment.domain;

public class TriggerCount {

    private boolean hemoglobin;
    private boolean microalbumin;
    private boolean bodyHeight;
    private boolean bodyWeight;
    private boolean smoker;
    private boolean abnormal;
    private boolean cholesterol;
    private boolean dizziness;
    private boolean relapse;
    private boolean reaction;
    private boolean antibodies;

    public void addHemoglobin() {
        hemoglobin = true;
    }
    public void addMicroalbumin() {
        microalbumin = true;
    }
    public void addBodyHeight() {
        bodyHeight = true;
    }
    public void addBodyWeight() {
        bodyWeight = true;
    }
    public void addSmoker() {
        smoker = true;
    }
    public void addAbnormal() {
        abnormal = true;
    }
    public void addCholesterol() {
        cholesterol = true;
    }
    public void addDizziness() {
        dizziness = true;
    }
    public void addRelapse() {
        relapse = true;
    }
    public void addReaction() {
        reaction = true;
    }
    public void addAntibodies() {
        antibodies = true;
    }

    public int getTriggerCount() {
        int count = 0;
        boolean[] triggers = {hemoglobin,microalbumin,bodyHeight,bodyWeight,smoker,
        abnormal,cholesterol,dizziness,relapse,reaction,antibodies};
        for (boolean trigger : triggers) {
            if (trigger) {
                count++;
            }
        }
        return count;
    }
}
