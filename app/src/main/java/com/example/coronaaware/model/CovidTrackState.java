package com.example.coronaaware.model;

public class CovidTrackState {

    String stateName, confirmedCasesIndian, confirmedCasesForeign, discharged, deaths;

    public CovidTrackState(String stateName, String confirmedCasesIndian, String confirmedCasesForeign, String discharged, String deaths) {
        this.stateName = stateName;
        this.confirmedCasesIndian = confirmedCasesIndian;
        this.confirmedCasesForeign = confirmedCasesForeign;
        this.discharged = discharged;
        this.deaths = deaths;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getConfirmedCasesIndian() {
        return confirmedCasesIndian;
    }

    public void setConfirmedCasesIndian(String confirmedCasesIndian) {
        this.confirmedCasesIndian = confirmedCasesIndian;
    }

    public String getConfirmedCasesForeign() {
        return confirmedCasesForeign;
    }

    public void setConfirmedCasesForeign(String confirmedCasesForeign) {
        this.confirmedCasesForeign = confirmedCasesForeign;
    }

    public String getDischarged() {
        return discharged;
    }

    public void setDischarged(String discharged) {
        this.discharged = discharged;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }
}
