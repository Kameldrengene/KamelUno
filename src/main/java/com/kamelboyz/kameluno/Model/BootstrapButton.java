package com.kamelboyz.kameluno.Model;

import javafx.scene.control.Button;

public class BootstrapButton {

    public static Button makeBootstrapButton(String text, String buttonColor) {
        Button button = new Button(text);
        button.getStyleClass().setAll("btn", buttonColor);
        button.setStyle("-fx-alignment: CENTER;");
        return button;
    }
}
