module com.kamelboyz.kameluno {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires lombok;
    requires common;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;


    opens com.kamelboyz.kameluno to javafx.fxml;
    exports com.kamelboyz.kameluno;
    exports com.kamelboyz.kameluno.Model;
}