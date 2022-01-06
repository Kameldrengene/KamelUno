package com.kamelboyz.kameluno;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.ArrayList;

public class MainApp extends Application {

    private ArrayList<Button> buttons = new ArrayList<>();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {


        //Creating objects
        Text text = new Text();
        buttons.add(makeBootstrapButton("Play", "btn-success"));
        buttons.add(makeBootstrapButton("Quit", "btn-danger"));
        buttons.add(makeBootstrapButton("Settings", "btn-info"));

        //Setting the text to be added.
        text.setText("Kamel UNO");
        text.setFont(new Font(172));

        //setting the position of the text
        text.setTextAlignment(TextAlignment.CENTER);
        text.setX(bounds.getWidth()/2-text.getLayoutBounds().getWidth()/2);
        text.setY(bounds.getHeight()/7);
        text.setFill(Color.rgb(255, 255, 255, 0.7));
        Reflection reflection = new Reflection();
        reflection.setTopOffset(-33);
        reflection.setTopOpacity(0.75);
        reflection.setBottomOpacity(0.0);
        reflection.setFraction(0.7);
        text.setEffect(reflection);
        alignButtonsInMiddle();

        //Creating a Group object
        Group root = new Group();
        root.getChildren().add(text);
        for (Button b : buttons) {
            root.getChildren().add(b);
        }
        //Creating a scene object
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        RadialGradient lg1 = new RadialGradient(0,
                .01,
                bounds.getWidth()/2,
                bounds.getHeight()/2,
                bounds.getWidth()/2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(85, 0, 0, 1)),
                new Stop(1, Color.BLACK));
        scene.setFill(lg1);
        exitOnEsc(scene);
        //Setting title to the Stage
        stage.setTitle("Sample Application");
        //stage.setFullScreen(true);
        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    private void alignButtonsInMiddle() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setMaxSize(bounds.getWidth(), bounds.getHeight() / 20);
            buttons.get(i).setMinSize(bounds.getWidth() / 2, bounds.getHeight() / 20);
            buttons.get(i).setPrefWidth(bounds.getWidth() / 2);
            buttons.get(i).setLayoutY(((bounds.getHeight() / (20)) + (i * 80)) + bounds.getHeight() / 3);
            buttons.get(i).setLayoutX((bounds.getWidth() / 4));
        }
    }

    private Button makeBootstrapButton(String text, String buttonColor) {
        Button button = new Button(text);
        button.getStyleClass().setAll("btn", buttonColor);
        button.setStyle("-fx-alignment: CENTER;");
        return button;
    }

    private void exitOnEsc(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    Stage sb = (Stage) scene.getWindow();
                    sb.close();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}