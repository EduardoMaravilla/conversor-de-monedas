package org.maravill.conversordemonedasfx.services;


import org.maravill.conversordemonedasfx.models.ConversionRate;
import org.maravill.conversordemonedasfx.models.Currency;
import org.maravill.conversordemonedasfx.models.SupportCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ExchangeService {

    private static List<ConversionRate> conversionRates = new ArrayList<>();

    private ExchangeService() {
    }
    static {
        List<ConversionRate> rates = FileService.loadConversionRateFromFile();
        if (!rates.isEmpty()) {
            conversionRates = new ArrayList<>(rates);
        }
    }

    public static List<Currency> getCurrencyCodes() {
        List<Currency> codes = new ArrayList<>(FileService.getSupportCodesFromFile());
        if (codes.isEmpty()) {
            codes = new ArrayList<>(ExchangeRateApiService.getSupportCodesFromApi());
            boolean result = FileService.saveSupportCodesToFile(new SupportCodes(codes, System.currentTimeMillis()));
            return result ? codes : Collections.emptyList();
        } else {
            return codes;
        }
    }

    public static ConversionRate getConversionRates(String baseCode) {
        long nowDate = System.currentTimeMillis();
        long daysThirtyInMilis = 1000L * 60 * 60 * 24 * 30;

        Iterator<ConversionRate> iterator = conversionRates.iterator();
        while (iterator.hasNext()) {
            ConversionRate conversionRate = iterator.next();
            if (conversionRate.getBaseCode().equals(baseCode)) {
                long lastUpdate = conversionRate.getLastUpdate();
                if (nowDate - lastUpdate < daysThirtyInMilis) {
                    return conversionRate;
                } else {
                    iterator.remove();
                    break;
                }
            }
        }

        ConversionRate newRate = ExchangeRateApiService.getConversionRateFromBaseCode(baseCode);
        if (newRate != null) {
            conversionRates.add(newRate);
            FileService.saveNewConversionRate(newRate);
        }
        return newRate;
    }

}
