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
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExchangeRateApplication.class.getResource("exchange-rate-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setResizable(false);
        stage.setTitle("Conversor De Monedas");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icons.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}