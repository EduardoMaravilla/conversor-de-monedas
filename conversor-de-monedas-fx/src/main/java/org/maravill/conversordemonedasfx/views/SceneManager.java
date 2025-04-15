package org.maravill.conversordemonedasfx.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.maravill.conversordemonedasfx.ExchangeRateApplication;

import java.io.IOException;
import java.util.logging.Logger;

public final class SceneManager {
    private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());
    private static Stage stage;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 850;

    private SceneManager() {
    }

    public static void setStage(Stage mainStage) {
        stage = mainStage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ExchangeRateApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.severe("Error al cargar la vista: " + e.getLocalizedMessage());
        }
    }
}

