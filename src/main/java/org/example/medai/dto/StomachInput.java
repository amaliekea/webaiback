package org.example.medai.dto;

public class StomachInput {
    private String painLocation;
    private String duration;
    private String symptoms;
    private String recentFood;
    private String recentIllness;


    public String getPainLocation() {
        return painLocation;
    }

    public void setPainLocation(String painLocation) {
        this.painLocation = painLocation;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getRecentFood() {
        return recentFood;
    }

    public void setRecentFood(String recentFood) {
        this.recentFood = recentFood;
    }

    public String getRecentIllness() {
        return recentIllness;
    }

    public void setRecentIllness(String recentIllness) {
        this.recentIllness = recentIllness;
    }
}