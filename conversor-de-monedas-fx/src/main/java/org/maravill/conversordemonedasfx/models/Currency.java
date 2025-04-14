package org.maravill.conversordemonedasfx.models;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Currency implements Comparable<Currency>{
    private String currencyCode;
    private String currencyName;

    public Currency() {
    }

    public Currency(String currencyCode, String currencyName) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(currencyCode, currency.currencyCode)
            && Objects.equals(currencyName, currency.currencyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, currencyName);
    }


    @Override
    public int compareTo(@NotNull Currency o) {
        return this.currencyCode.compareTo(o.currencyCode);
    }

    @Override
    public String toString() {
        return "["+this.currencyCode +"] "+this.currencyName;
    }
}
