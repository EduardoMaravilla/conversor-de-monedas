package org.maravill.conversordemonedasfx.views;

public enum PageViews {
    HOME("api-key-view.fxml"),
    CONVERSOR("exchange-rate-view.fxml");
    private final String fxmlPath;
    PageViews(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
    public String getFxmlPath() {
        return fxmlPath;
    }
}
