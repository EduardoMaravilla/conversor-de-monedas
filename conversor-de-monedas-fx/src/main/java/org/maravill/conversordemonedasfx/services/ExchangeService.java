package org.maravill.conversordemonedasfx.services;


import org.maravill.conversordemonedasfx.models.ConversionRate;
import org.maravill.conversordemonedasfx.models.Currency;
import org.maravill.conversordemonedasfx.models.SupportCodes;

import java.util.ArrayList;
import java.util.Collections;
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
            codes = new ArrayList<>(ExchangeRateApi.getSupportCodesFromApi());
            boolean result = FileService.saveSupportCodesToFile(new SupportCodes(codes));
            return result ? codes : Collections.emptyList();
        } else {
            return codes;
        }
    }

    public static ConversionRate getConversionRates(String baseCode) {
        for (ConversionRate conversionRate : conversionRates) {
            if (conversionRate.getBaseCode().equals(baseCode)){
                return conversionRate;
            }
        }
        ConversionRate conversionRate = ExchangeRateApi.getConversionRateFromBaseCode(baseCode);
        if (conversionRate != null) {
            conversionRates.add(conversionRate);
            FileService.saveNewConversionRate(conversionRate);
        }
        return conversionRate;
    }

}
