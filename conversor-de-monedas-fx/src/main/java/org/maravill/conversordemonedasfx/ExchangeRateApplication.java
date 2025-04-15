package org.maravill.conversordemonedasfx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.maravill.conversordemonedasfx.services.FileService;
import org.maravill.conversordemonedasfx.views.PageViews;
import org.maravill.conversordemonedasfx.views.SceneManager;

import java.io.IOException;
import java.util.Objects;

public class ExchangeRateApplication extends Application {

    private static final String TITLE = "Conversor De Monedas";
    private static final String ICON = "/images/icons.png";

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.setTitle(TITLE);
        stage.getIcons().add(
            new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(ICON))));
        SceneManager.setStage(stage);
        boolean isValidApiKey = FileService.existsValidApiKey();
        if (isValidApiKey) {
            SceneManager.switchScene(PageViews.CONVERSOR.getFxmlPath());
        } else {
            SceneManager.switchScene(PageViews.HOME.getFxmlPath());
        }
    }
}