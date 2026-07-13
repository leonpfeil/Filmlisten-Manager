package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ScrapingInstructions {
    @JsonProperty("URL")
    private String URL;
    @JsonProperty("Obergrenze")
    private int Obergrenze;

    public String getURL() {
        return URL;
    }

    public void SetURL(String URL) {
        this.URL = URL;
    }

    public int getObergrenze() {
        return Obergrenze;
    }

    public void SetObergrenze(int Obergrenze) {
        this.Obergrenze = Obergrenze;
    }

}
