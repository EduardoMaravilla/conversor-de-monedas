package org.maravill.conversordemonedasfx;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof IllegalArgumentException
                && throwable.getMessage().contains("start must be <= the end")) {
            } else {
                throwable.printStackTrace();
            }
        });
        Application.launch(ExchangeRateApplication.class, args);
    }
}
