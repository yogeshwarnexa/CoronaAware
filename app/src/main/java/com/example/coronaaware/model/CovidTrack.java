package com.example.coronaaware.model;

public class CovidTrack {

    String countouryName;
    int confirmed;
    int deaths;
    int recovered;

    String lat;
    String lng;
    String conutryCode;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getConutryCode() {
        return conutryCode;
    }

    public void setConutryCode(String conutryCode) {
        this.conutryCode = conutryCode;
    }

    public String getCountouryName() {
        return countouryName;
    }

    public void setCountouryName(String countouryName) {
        this.countouryName = countouryName;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
}
