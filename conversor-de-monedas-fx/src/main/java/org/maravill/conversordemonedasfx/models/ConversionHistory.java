package org.maravill.conversordemonedasfx.models;

import javafx.beans.property.SimpleStringProperty;

public class ConversionHistory {
    private final SimpleStringProperty date;
    private final SimpleStringProperty originCoin;
    private final SimpleStringProperty destinyCoin;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty conversion;

    public ConversionHistory(String date, String originCoin, String destinyCoin, String amount, String conversion) {
        this.date = new SimpleStringProperty(date);
        this.originCoin = new SimpleStringProperty(originCoin);
        this.destinyCoin = new SimpleStringProperty(destinyCoin);
        this.amount = new SimpleStringProperty(amount);
        this.conversion = new SimpleStringProperty(conversion);
    }

    public String getDate() {
        return date.get();
    }
    public String getOriginCoin() {
        return originCoin.get();
    }

    public String getDestinyCoin() {
        return destinyCoin.get();
    }

    public String getAmount() {
        return amount.get();
    }

    public String getConversion() {
        return conversion.get();
    }
}

