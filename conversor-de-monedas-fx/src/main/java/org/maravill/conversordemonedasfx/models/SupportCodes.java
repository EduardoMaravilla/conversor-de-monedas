package org.maravill.conversordemonedasfx.models;

import java.util.ArrayList;
import java.util.List;

public class SupportCodes {

    private List<Currency> coinSupportCodes;

    public SupportCodes() {
        this.coinSupportCodes = new ArrayList<>();
    }

    public SupportCodes(List<Currency> coinSupportCodes) {
        this.coinSupportCodes = new ArrayList<>(coinSupportCodes);
    }

    public List<Currency> getCoinSupportCodes() {
        return coinSupportCodes;
    }

    public void setCoinSupportCodes(List<Currency> coinSupportCodes) {
        this.coinSupportCodes = new ArrayList<>(coinSupportCodes);
    }

    public void addCoinSupportCode(Currency currency){
        this.coinSupportCodes.add(currency);
    }
}
