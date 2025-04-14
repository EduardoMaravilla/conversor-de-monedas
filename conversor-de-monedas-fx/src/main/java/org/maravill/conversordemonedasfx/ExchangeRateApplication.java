package org.maravill.conversordemonedasfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.Objects;

public class ExchangeRateApplication extends Application {

    private static final String TITLE = "Conversor De Monedas";
    private static final String ICON = "/images/icons.png";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final String VIEW_RESOURCE = "/exchange-rate-view.fxml";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
            new FXMLLoader(ExchangeRateApplication.class.getResource(VIEW_RESOURCE));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setTitle(TITLE);
        stage.getIcons().add(
            new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream(ICON))));
        stage.setScene(scene);
        stage.show();
    }
}