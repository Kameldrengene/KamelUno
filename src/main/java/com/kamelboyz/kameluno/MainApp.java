package com.kamelboyz.kameluno;

import com.kamelboyz.kameluno.Controller.ScreenController;
import com.kamelboyz.kameluno.KeyPress.OnEscape;
import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.kamelboyz.kameluno.Model.LobbyListHandler;
import com.kamelboyz.kameluno.ModelView.LobbyView;
import com.kamelboyz.kameluno.ModelView.MainView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.kamelboyz.kameluno.Model.BootstrapButton.makeBootstrapButton;

public class MainApp extends Application {

    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {

        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getRoot(), bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        RadialGradient lg1 = new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK));
        scene.setFill(lg1);
        OnEscape.exitOnEsc(scene);

        //Setting title to the Stage
        stage.setTitle("Kamel Ludo");
        stage.setFullScreen(true);
        //Adding scene to the stage
        stage.setScene(scene);
        ScreenController.getInstance().init(scene,stage);
        ScreenController.getInstance().addScreen("main",mainView.getRoot());
        mainView.initializeButtonClicks();
        //Displaying the contents of the stage
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }
}