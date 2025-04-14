package org.maravill.conversordemonedasfx.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.maravill.conversordemonedasfx.models.ConversionRate;
import org.maravill.conversordemonedasfx.models.Currency;
import org.maravill.conversordemonedasfx.models.SupportCodes;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class FileService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SUPPORT_CODES_FILE = "supportCodesCurrenciesExchangeRate.json";
    private static final String CONVERSION_RATE_FILE = "conversionRateCodeExchangeRate.json";
    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private FileService() {
    }

    static {
        createFile(SUPPORT_CODES_FILE);
        createFile(CONVERSION_RATE_FILE);
    }

    public static List<Currency> getSupportCodesFromFile() {
        File file = new File(TEMP_PATH, SUPPORT_CODES_FILE);
        if (!file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }

        try {
            SupportCodes supportCodes = readFromJson(file, SupportCodes.class);
            return supportCodes.getCoinSupportCodes();
        } catch (IOException e) {
            LOGGER.warning("No se pudo leer el archivo: " + e.getLocalizedMessage());
            return Collections.emptyList();
        }
    }

    public static boolean saveSupportCodesToFile(SupportCodes supportCodes) {
        File file = new File(TEMP_PATH, SUPPORT_CODES_FILE);
        try {
            writeToJson(file, supportCodes);
            return true;
        } catch (IOException e) {
            LOGGER.severe("Error al guardar el archivo: " + e.getLocalizedMessage());
            return false;
        }
    }

    private static <T> T readFromJson(File file, Class<T> clazz) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return objectMapper.readValue(inputStream, clazz);
        }
    }

    private static <T> T readFromJson(File file, TypeReference<T> type) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return objectMapper.readValue(inputStream,type);
        }
    }

    private static void writeToJson(File file, Object data) throws IOException {
        objectMapper.writeValue(file, data);
    }

    public static void saveNewConversionRate(ConversionRate conversionRate) {
        List<ConversionRate> rates = loadConversionRateFromFile();
        if (rates.isEmpty()) {
            rates = new ArrayList<>();
        }
        rates.add(conversionRate);
        try {
            writeToJson(new File(TEMP_PATH, CONVERSION_RATE_FILE), rates);
        } catch (IOException e) {
            LOGGER.severe("Error al guardar el archivo: " + e.getLocalizedMessage());
        }
    }

    private static void createFile(String fileName) {
        File file= new File(TEMP_PATH, fileName);
        try {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    LOGGER.info("Directorio creado correctamente");
                }
            }

            if (!file.exists()) {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) {
                    LOGGER.info("Archivo creado correctamente: " + file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error al inicializar el archivo de soporte: " + e.getLocalizedMessage());
        }
    }

    public static List<ConversionRate> loadConversionRateFromFile() {
        File file = new File(TEMP_PATH, CONVERSION_RATE_FILE);
        if (!file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }
        try {
            return readFromJson(file, new TypeReference<>() {});
        } catch (IOException e) {
            LOGGER.warning("No se pudo leer el archivo: " + e.getLocalizedMessage());
            return Collections.emptyList();
        }
    }
}
