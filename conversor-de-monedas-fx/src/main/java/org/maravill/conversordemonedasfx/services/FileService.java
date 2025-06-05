package org.maravill.conversordemonedasfx.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.maravill.conversordemonedasfx.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public final class FileService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SUPPORT_CODES_FILE = "supportCodesCurrenciesExchangeRate.json";
    private static final String CONVERSION_RATE_FILE = "conversionRateCodeExchangeRate.json";
    private static final String API_KEY_FILE = "apiKey.json";
    private static final String HISTORY_CONVERSIONS_FILE = "conversionHistory.json";
    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    private FileService() {
    }

    static {
        createFile(SUPPORT_CODES_FILE);
        createFile(CONVERSION_RATE_FILE);
        createFile(API_KEY_FILE);
        createFile(HISTORY_CONVERSIONS_FILE);
    }

    public static List<Currency> getSupportCodesFromFile() {
        File file = new File(TEMP_PATH, SUPPORT_CODES_FILE);
        if (!file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }

        try {
            SupportCodes supportCodes = readFromJson(file, SupportCodes.class);
            long nowDate = System.currentTimeMillis();
            long daysThirtyInMilis = 1000L * 60 * 60 * 24 * 30;
            if (supportCodes.getLastUpdate() == null){
                supportCodes.setLastUpdate(nowDate);
            }
            return (nowDate - daysThirtyInMilis < supportCodes.getLastUpdate()) ?
                supportCodes.getCoinSupportCodes() : Collections.emptyList();
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
            return objectMapper.readValue(inputStream, type);
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
        File file = new File(TEMP_PATH, fileName);
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
            return readFromJson(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            LOGGER.warning("No se pudo leer el archivo: " + e.getLocalizedMessage());
            return Collections.emptyList();
        }
    }

    public static boolean saveApiKey(String apiKey) {
        Information information = new Information(apiKey);
        File file = new File(TEMP_PATH, API_KEY_FILE);
        try {
            writeToJson(file, information);
            return true;
        } catch (IOException e) {
            LOGGER.severe("Error al guardar el archivo: " + e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean existsValidApiKey() {
        File file = new File(TEMP_PATH, API_KEY_FILE);
        if (!file.exists() || file.length() == 0) {
            return false;
        }
        try {
            Information information = readFromJson(file, Information.class);
            return ExchangeRateApiService.validateApiKey(information.getApiKey());
        } catch (IOException e) {
            LOGGER.warning("No se pudo leer el archivo: " + e.getLocalizedMessage());
            return false;
        }
    }

    public static void saveHistoryConversions(List<ConversionHistory> conversionHistories) {
        try {
            writeToJson(new File(TEMP_PATH, HISTORY_CONVERSIONS_FILE), conversionHistories);
        } catch (IOException e) {
            LOGGER.severe("Error al guardar el archivo: " + e.getLocalizedMessage());
        }
    }

    public static List<ConversionHistory> loadConversionHistoryFromFile() {
        File file = new File(TEMP_PATH, HISTORY_CONVERSIONS_FILE);
        if (!file.exists() || file.length() == 0) {
            return Collections.emptyList();
        }
        try {

            List<ConversionHistoryDTO> conversionHistories = readFromJson(file, new TypeReference<>() {});

            return conversionHistories.stream()
                .map(dto -> new ConversionHistory(
                    dto.getDate(),
                    dto.getOriginCoin(),
                    dto.getDestinyCoin(),
                    dto.getAmount(),
                    dto.getConversion()
                ))
                .toList();
        } catch (IOException e) {
            LOGGER.warning("No se pudo leer el archivo: " + e.getLocalizedMessage());
            return Collections.emptyList();
        }
    }
}
