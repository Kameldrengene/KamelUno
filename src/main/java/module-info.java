module com.kamelboyz.kameluno {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires lombok;


    opens com.kamelboyz.kameluno to javafx.fxml;
    exports com.kamelboyz.kameluno;
}