module org.maravill.conversordemonedasfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires annotations;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires java.logging;

    opens org.maravill.conversordemonedasfx to javafx.fxml;
    exports org.maravill.conversordemonedasfx;
    exports org.maravill.conversordemonedasfx.controller;
    opens org.maravill.conversordemonedasfx.controller to javafx.fxml;
    exports org.maravill.conversordemonedasfx.models;
}