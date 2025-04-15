package org.maravill.conversordemonedasfx.models;

public class Information {

    private String apiKey;

    public Information() {
    }
    public Information(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
