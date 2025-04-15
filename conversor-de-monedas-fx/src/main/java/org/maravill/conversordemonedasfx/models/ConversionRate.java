package org.maravill.conversordemonedasfx.models;


import java.util.Map;

public class ConversionRate {
    private String baseCode;
    private Map<String,Double> conversionRates;
    private Long lastUpdate;

    public ConversionRate() {
    }

    public ConversionRate(String baseCode, Map<String, Double> conversionRates, Long lastUpdate) {
        this.baseCode = baseCode;
        this.conversionRates = conversionRates;
        this.lastUpdate = lastUpdate;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
