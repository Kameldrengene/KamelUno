package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Model.BootstrapButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

@Data
public class WinView {
    private VBox vBox = new VBox(10);

    public WinView(double width,double height) throws IOException {
        InputStream stream = getClass().getResource("/images/win.png").openStream();
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width*0.3);
        imageView.setFitHeight(height*0.3);

        Button afslut = BootstrapButton.makeBootstrapButton("afslut","btn-success");
        afslut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // change scene
            }
        });

        vBox.getChildren().addAll(imageView,afslut);
        vBox.setAlignment(Pos.CENTER);
    }

}
