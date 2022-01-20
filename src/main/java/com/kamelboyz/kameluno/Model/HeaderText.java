package com.kamelboyz.kameluno.Model;

import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

public class HeaderText {

    public static Text setTextProperties(Text text) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        text.setFont(new Font(172));

        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(bounds.getWidth() / 2 - text.getLayoutBounds().getWidth() / 2);
        text.setY(bounds.getHeight() / 6);
        text.setFill(Color.rgb(255, 255, 255, 0.7));
//        Reflection reflection = new Reflection();
//        reflection.setTopOffset(0);
//        reflection.setTopOpacity(0.75);
//        reflection.setBottomOpacity(0.0);
//        reflection.setFraction(0.7);
//        text.setEffect(reflection);
        return text;
    }
}
