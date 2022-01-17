package com.kamelboyz.kameluno.ModelView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class GameView {
    private Map<String, String> opponents = new HashMap<>();
    private Map<String,Integer> opponentsPosition = new HashMap<>();
    private List<Card> playerCards = new ArrayList<>();
    private final GridPane grid = new GridPane();
    private final Pane pane = new Pane();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private List<ImageView> pileImages = new ArrayList<>();
    private Map<String,ImageView> playerCardsImages = new HashMap<>();



    public GameView() {
        pane.setPrefSize(bounds.getWidth(), bounds.getHeight());
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(bgFill));
        initPlayerCards();
        initLayouts();
    }


    public void initLayouts() {

        // root layout
        HBox rootLayout = new HBox(5);
        rootLayout.setPrefSize(bounds.getWidth(), bounds.getHeight());
        rootLayout.setPadding(new Insets(5, 5, 5, 5));

        //create left, middle and right vertical boxes
        try {
            rootLayout.getChildren().addAll(createLeftBox(), createMiddleBox(), createRightBox());
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.getChildren().addAll(rootLayout);

    }

    public void initPlayerCards() {
        opponents.put("magic mike","7");
        opponents.put("volkan","7");
        opponents.put("mark","7");
        int i = 1;
        for (String key:opponents.keySet()) {
            opponentsPosition.put(key,i);
            i++;
        }
        playerCards.add(new Card("Red", "1"));
        playerCards.add(new Card("Blue", "1"));
        playerCards.add(new Card("Green", "1"));
        playerCards.add(new Card("Red", "2"));
        playerCards.add(new Card("Blue", "2"));
        playerCards.add(new Card("Green", "2"));
        playerCards.add(new Card("Yellow", "2"));

        for (Card card:playerCards) {
            try {
                String cardName = card.getColor()+"_"+card.getValue();
                System.out.println(cardName);
                ImageView imageView = getImage(cardName,Type.HORIZONTAL);
                playerCardsImages.put(cardName,imageView);
                System.out.println("putting");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public VBox createLeftBox() {
        VBox leftVBox = new VBox(200);
        leftVBox.setAlignment(Pos.CENTER);
        leftVBox.setPrefWidth(bounds.getWidth() * 0.2);
        leftVBox.setBackground(new Background(new BackgroundFill(
                new RadialGradient(
                        0, 0, 0.5, 0.5, 0.5, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#FFFFFF33")),
                        new Stop(1, Color.web("#00000033"))),
                CornerRadii.EMPTY, Insets.EMPTY
        )));

        Button button = new Button("click");
        Button button1 = new Button("click1");
        Button button2 = new Button("click2");

        leftVBox.getChildren().addAll(button, button1, button2);
        return leftVBox;
    }

    public BorderPane createMiddleBox() throws IOException {
        BorderPane midBox = new BorderPane();

        midBox.setPrefWidth(bounds.getWidth() * 0.6);

        midBox.setBackground(new Background(new BackgroundFill(
                new RadialGradient(
                        0, 0, 0.5, 0.5, 0.5, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#FFFFFF33")),
                        new Stop(1, Color.web("#00000033"))),
                CornerRadii.EMPTY, Insets.EMPTY
        )));

        // data of each layout
        Button button = new Button("click");
        Button button1 = new Button("click1");
        Button button2 = new Button("click2");
        Button button3 = new Button("click3");

        // create bottom layout of cards
        HBox bottumlayout = new HBox(10);
        for (String key:playerCardsImages.keySet()) {
            ImageView imageView = playerCardsImages.get(key);
            bottumlayout.getChildren().add(imageView);
        }

        bottumlayout.setAlignment(Pos.CENTER);
        midBox.setBottom(bottumlayout);

        // create top layout of cards
        HBox toplayout = new HBox(10);
        toplayout.getChildren().addAll(button);
        bottumlayout.setAlignment(Pos.CENTER);
        midBox.setTop(toplayout);

        // create middle layout of deck and pile
        midBox.setCenter(button1);

        return midBox;
    }

    public BorderPane createRightBox() {
        BorderPane rightBox = new BorderPane();
        rightBox.setPrefWidth(bounds.getWidth() * 0.2);
        rightBox.setBackground(new Background(new BackgroundFill(
                new RadialGradient(
                        0, 0, 0.5, 0.5, 0.5, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#FFFFFF33")),
                        new Stop(1, Color.web("#00000033"))),
                CornerRadii.EMPTY, Insets.EMPTY
        )));

        VBox rightPlayerCards = new VBox(5);
        for (String name:opponentsPosition.keySet()) {
            int pos = opponentsPosition.get(name);
            if(pos==3){
                int nrCards = Integer.parseInt(opponents.get(name));
                System.out.println("in here");
                for (int i = 0; i < nrCards; i++) {
                    try {
                        rightPlayerCards.getChildren().add(getImage("Deck",Type.VERTICAL));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        rightPlayerCards.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setCenter(rightPlayerCards);
        return rightBox;
    }


    public ImageView getImage(String cardName,Type type) throws IOException {
        //creating the image object
        InputStream stream = getClass().getResource("/images/" + cardName+".png").openStream();
        Image image = new Image(stream);
        //Creating the image view
        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(image);
        if(type == Type.HORIZONTAL){
            imageView.setFitWidth(bounds.getWidth() * 0.06);
            imageView.setFitHeight(bounds.getWidth() * 0.08);
        }else {
            imageView.setFitWidth(bounds.getWidth() * 0.08);
            imageView.setFitHeight(bounds.getWidth() * 0.06);
        }

        return imageView;
    }

    public void updatePlayerCards() {

    }


}

@Data
@AllArgsConstructor
class Card{
    private String color;
    private String value;
}

enum Type{
    VERTICAL,
    HORIZONTAL
}
