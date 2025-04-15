package org.maravill.conversordemonedasfx.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.maravill.conversordemonedasfx.services.ExchangeRateApiService;
import org.maravill.conversordemonedasfx.views.PageViews;
import org.maravill.conversordemonedasfx.views.SceneManager;

import java.awt.*;
import java.net.URI;
import java.util.logging.Logger;

public class InitialPageController {

    private static final Logger LOGGER = Logger.getLogger(InitialPageController.class.getName());

    private static final String DESCRIPTION_TEXT = "Conversor de Monedas FX es un proyecto desarrollado " +
        "como parte del \"Challenge Conversor de Monedas\". " +
        "Este desafío tiene como objetivo aplicar los conocimientos " +
        "adquiridos en Java, poniendo en práctica conceptos fundamentales " +
        "de la programación orientada a objetos. La aplicación permite " +
        "convertir valores entre distintas monedas en tiempo real, haciendo " +
        "uso de la API de \"ExchangeRate-API\". El proyecto se enfoca tanto " +
        "en la lógica del backend como en el diseño de una interfaz gráfica funcional " +
        "mediante JavaFX, promoviendo el desarrollo de aplicaciones robustas y orientadas a soluciones reales.";
    private static final int PAUSE_DURATION = 5;
    private static final String STYLE_WARNING = "alert-warning";
    private static final String STYLE_DANGER = "alert-danger";

    public TextFlow descriptionText;
    public Button apiKeyButton;
    public TextField apiKeyText;
    public Button continueButton;
    public TextField messageText;

    @FXML
    public void initialize() {
        setDescriptionText();
        setApiKeyButton();
        setContinueButton();
    }

    private void setContinueButton() {
        continueButton.setOnAction(e -> {
            String apiKey = apiKeyText.getText();
            if (apiKey.isEmpty()) {
                showMessage("Debe ingresar una clave de API valida", STYLE_WARNING);
                return;
            }
            boolean isValidApiKey = ExchangeRateApiService.validateApiKey(apiKey);
            if (!isValidApiKey) {
                showMessage("Api Key no valida", STYLE_DANGER);
                return;
            }
            SceneManager.switchScene(PageViews.CONVERSOR.getFxmlPath());
        });
    }

    private void setApiKeyButton() {
        apiKeyButton.setOnAction(e -> {
            try {
                URI uri = new URI("https://www.exchangerate-api.com/");
                Desktop.getDesktop().browse(uri);
            } catch (Exception ex) {
                LOGGER.severe("Error al abrir el enlace: " + ex.getLocalizedMessage());
            }
        });
    }

    private void setDescriptionText() {
        Text text = new Text(DESCRIPTION_TEXT);
        text.setFill(Color.web("#F4F4F5"));
        descriptionText.getChildren().add(text);
    }

    private void showMessage(String message, String style) {
        messageText.getStyleClass().setAll("h4", "alert", style);
        messageText.setVisible(true);
        messageText.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(PAUSE_DURATION));
        pause.setOnFinished(e -> messageText.setVisible(false));
        pause.play();
    }
}
