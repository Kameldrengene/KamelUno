package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Model.BootstrapButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Data;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.io.InputStream;

@Data
public class LoseView {
    VBox vBox = new VBox(20);
    public LoseView(String winnerName, double width, double height) throws IOException {
        InputStream stream = getClass().getResource("/images/lose.png").openStream();
        Image image = new Image(stream);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(width*0.3);
        imageView.setFitHeight(height*0.3);

        Text name = new Text();
        name.setText(winnerName + " won the Game");
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(width*0.5);

        Button afslut = BootstrapButton.makeBootstrapButton("afslut","btn-success");
        afslut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // change scene
            }
        });

        vBox.getChildren().addAll(imageView,name,afslut);
        vBox.setAlignment(Pos.CENTER);
    }
}
