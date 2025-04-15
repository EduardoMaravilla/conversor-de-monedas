package org.maravill.conversordemonedasfx.models;

public class ConversionHistoryDTO {
    private String date;
    private String originCoin;
    private String destinyCoin;
    private String amount;
    private String conversion;

    public ConversionHistoryDTO() {
    }

    public ConversionHistoryDTO(String date, String originCoin, String destinyCoin, String amount, String conversion) {
        this.date = date;
        this.originCoin = originCoin;
        this.destinyCoin = destinyCoin;
        this.amount = amount;
        this.conversion = conversion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOriginCoin() {
        return originCoin;
    }

    public void setOriginCoin(String originCoin) {
        this.originCoin = originCoin;
    }

    public String getDestinyCoin() {
        return destinyCoin;
    }

    public void setDestinyCoin(String destinyCoin) {
        this.destinyCoin = destinyCoin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }
}
