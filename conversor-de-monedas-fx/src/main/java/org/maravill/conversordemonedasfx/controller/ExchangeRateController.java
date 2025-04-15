package org.maravill.conversordemonedasfx.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.maravill.conversordemonedasfx.models.ConversionHistory;
import org.maravill.conversordemonedasfx.models.ConversionRate;
import org.maravill.conversordemonedasfx.models.Currency;
import org.maravill.conversordemonedasfx.services.ExchangeService;
import org.maravill.conversordemonedasfx.services.FileService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExchangeRateController {

    private static final int VISIBLE_ROWS = 10;
    private static final int BASE_CODE_START = 1;
    private static final int BASE_CODE_END = 4;
    private static final int PAUSE_DURATION = 10;
    private static final String STYLE_SUCCESS = "alert-success";
    private static final String STYLE_WARNING = "alert-warning";
    private static final String STYLE_DANGER = "alert-danger";
    private static final int MAX_HISTORY_SIZE = 500;

    public TextField messageSuccessful;
    public Button exchangeButton;
    public ComboBox<String> firstCoin;
    public ComboBox<String> secondCoin;
    public TextField amountToExchange;
    public TextField amountExchange;
    public TableColumn<ConversionHistory, String> historyDate;
    public TableColumn<ConversionHistory, String> historyOriginCoin;
    public TableColumn<ConversionHistory, String> historyDestinyCoin;
    public TableColumn<ConversionHistory, String> historyAmount;
    public TableColumn<ConversionHistory, String> historyConversion;
    public TableView<ConversionHistory> historyTable;
    public Button buttonClearHistory;
    public TextField author;
    public TextField historyLabel;


    private ObservableList<String> supportsCoins;
    private ObservableList<ConversionHistory> history;

    @FXML
    public void initialize() {

        author.setText("© " + LocalDate.now().getYear() + " EDUARDO MARAVILLA");

        setInitialHistory();

        // Obtiene las monedas soportadas
        List<Currency> currencyCodes = ExchangeService.getCurrencyCodes();
        List<String> currencyNames = new ArrayList<>();
        Collections.sort(currencyCodes);
        currencyCodes.forEach(coin -> currencyNames.add(coin.toString()));
        this.supportsCoins = FXCollections.observableList(currencyNames);

        firstCoin.setItems(supportsCoins);
        secondCoin.setItems(supportsCoins);
        addListenerComboBox(firstCoin);
        addListenerComboBox(secondCoin);
        addEventButtonConversion();
        addEventButtonClearHistory();

        historyDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        historyOriginCoin.setCellValueFactory(new PropertyValueFactory<>("originCoin"));
        historyDestinyCoin.setCellValueFactory(new PropertyValueFactory<>("destinyCoin"));
        historyAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        historyConversion.setCellValueFactory(new PropertyValueFactory<>("conversion"));

    }

    private void setInitialHistory() {
        history = FXCollections.observableArrayList();
        List<ConversionHistory> conversionHistories = FileService.loadConversionHistoryFromFile();
        if (!conversionHistories.isEmpty()) {
            history = FXCollections.observableArrayList(conversionHistories);
        }
        historyTable.setItems(history);
        if (!history.isEmpty()) {
            historyLabel.setText("HISTORIAL (" + history.size() + ")");
        }else {
            historyLabel.setText("HISTORIAL");
        }
    }

    private void addListenerComboBox(ComboBox<String> comboBox) {
        comboBox.setEditable(true);
        FilteredList<String> filteredItems = new FilteredList<>(supportsCoins, p -> true);
        comboBox.setItems(filteredItems);

        comboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = comboBox.getEditor();
            final String selected = comboBox.getSelectionModel().getSelectedItem();

            // Filtrar solo si el texto ingresado es diferente a la selección actual
            if (selected == null || !selected.equals(editor.getText())) {
                filteredItems.setPredicate(item -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    return item.toLowerCase().contains(lowerCaseFilter);
                });

                // Mostrar el ComboBox si hay coincidencias
                if (!filteredItems.isEmpty()) {
                    comboBox.show();
                    comboBox.setVisibleRowCount(Math.min(filteredItems.size(), VISIBLE_ROWS));
                } else {
                    comboBox.hide();
                }
            }
        });

        comboBox.getEditor().setOnMouseClicked(mouseEvent -> {
            comboBox.getEditor().setText("");
            comboBox.valueProperty().setValue(null);
            amountExchange.clear();
        });

    }

    private void addEventButtonConversion() {
        exchangeButton.setOnAction(e -> {
            String firstCoinValue = getTextFromComboBox(firstCoin);
            if (firstCoinValue.isEmpty()) {
                showMessage("Debe seleccionar una moneda de origen", STYLE_WARNING);
                return;
            }
            String secondCoinValue = getTextFromComboBox(secondCoin);
            if (secondCoinValue.isEmpty()) {
                showMessage("Debe seleccionar una moneda de destino", STYLE_WARNING);
                return;
            }
            double amountToExchangeValue = getAmountFromTextField(amountToExchange);
            if (amountToExchangeValue <= 0.0) {
                showMessage("Ingrese una cantidad valida o mayor a cero", STYLE_WARNING);
                return;
            }

            String firstCoinCode = getCoinCode(firstCoinValue);
            ConversionRate conversionRate = ExchangeService.getConversionRates(firstCoinCode);
            if (conversionRate == null) {
                showMessage("No se pudo obtener el valor de la moneda de origen", STYLE_DANGER);
                return;
            }

            String secondCoinCode = getCoinCode(secondCoinValue);
            double amountExchangeValue = setAmountExchange(conversionRate, secondCoinCode, amountToExchangeValue);

            String response = String.format(
                "El valor de %.3f [%s] corresponde al valor final de =>>> %.3f [%s]",
                amountToExchangeValue,
                firstCoinCode,
                amountExchangeValue,
                secondCoinCode
            );
            showMessage(response, STYLE_SUCCESS);
            updateHistoryTable(firstCoinValue, secondCoinValue, amountToExchangeValue, amountExchangeValue);
        });
    }

    private void addEventButtonClearHistory() {
        buttonClearHistory.setOnAction(e -> {
            history.clear();
            historyTable.setItems(history);
            historyLabel.setText("HISTORIAL");
            FileService.saveHistoryConversions(new ArrayList<>());
        });
    }

    private Double setAmountExchange(ConversionRate conversionRate,
                                     String secondCoinCode,
                                     double amountToExchangeValue) {
        double valueConversionRate = conversionRate.getConversionRates().get(secondCoinCode);
        double amountExchangeValue = amountToExchangeValue * valueConversionRate;
        amountExchange.setText(String.format("%.3f", amountExchangeValue));
        return amountExchangeValue;
    }

    private String getTextFromComboBox(ComboBox<String> comboBox) {
        return comboBox.getEditor().getText();
    }

    private Double getAmountFromTextField(TextField textField) {
        double amountToExchangeValue;
        try {
            amountToExchangeValue = Double.parseDouble(textField.getText());
        } catch (NumberFormatException ex) {
            amountToExchangeValue = 0.0;
        }
        return amountToExchangeValue;
    }

    private void showMessage(String message, String style) {
        messageSuccessful.getStyleClass().setAll("h4", "alert", style);
        messageSuccessful.setVisible(true);
        messageSuccessful.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_DURATION));
        pause.setOnFinished(e -> messageSuccessful.setVisible(false));
        pause.play();
    }

    private String getCoinCode(String coinName) {
        return coinName.substring(BASE_CODE_START, BASE_CODE_END);
    }

    private void updateHistoryTable(String originCoin, String destinyCoin,
                                    double amount, double conversion) {
        String date = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", System.currentTimeMillis());
        ConversionHistory conversionHistory =
            new ConversionHistory(date, originCoin, destinyCoin,
                String.format("%.3f", amount), String.format("%.3f", conversion));
        if (history == null) {
            history = FXCollections.observableArrayList();
        }

        if (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(MAX_HISTORY_SIZE - 1);
        }
        history.addFirst(conversionHistory);

        FileService.saveHistoryConversions(new ArrayList<>(history));
        historyLabel.setText("HISTORIAL (" + history.size() + ")");

        historyTable.setItems(history);
        historyTable.scrollTo(history.size() - 1);
    }
}