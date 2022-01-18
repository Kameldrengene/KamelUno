package com.kamelboyz.kameluno.ModelView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.*;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class GameView {

    // temp list to correctly place players
    private List<String> players = new ArrayList<>();

    // count each opponents' cards and position
    private Map<String, Opponent> opponents = new HashMap<>();

    private List<Card> playerCards = new ArrayList<>();
    private final GridPane grid = new GridPane();
    private final Pane pane = new Pane();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private List<ImageView> pileImages = new ArrayList<>();
    private Map<String,ImageView> playerCardsImages = new HashMap<>();

    private HBox playerCardsLayout = new HBox(10);
    private VBox opponent1CardsLayout = new VBox(5);
    private HBox opponent2CardsLayout = new HBox(10);
    private VBox opponent3CardsLayout = new VBox(5);

    public GameView() {
        pane.setPrefSize(bounds.getWidth(), bounds.getHeight());
        calculatePosition();
        BackgroundFill bgFill = new BackgroundFill(new RadialGradient(0, .01, bounds.getWidth() / 2, bounds.getHeight() / 2, bounds.getWidth() / 2, false, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(85, 0, 0, 1)), new Stop(1, Color.BLACK)), CornerRadii.EMPTY, Insets.EMPTY);
        InputStream stream = null;
        try {
            stream = getClass().getResource("/images/Table_1.png").openStream();
        } catch (IOException e) {

        }
        Image image = new Image(stream);
        pane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(bounds.getWidth(),bounds.getHeight(),false,false,false,false))));
        initPlayerCards();
        initLayouts();
    }

    public void calculatePosition(){
        players.add("mike");
        players.add("volkan");
        players.add("mark");
        players.add("talha");

        //find your position in array
        int myIndex = 2;

        for (int i = 0; i < players.size() ; i++) {
            int index = (myIndex + i) % players.size();
            String name = players.get(index);

            // if it's an opponent
            if(i!=0){
                opponents.put(name,new Opponent(name,i,7));
            }
        }
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

        // temp cards
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
                ImageView imageView = getImage(cardName,Type.HORIZONTAL);
                playerCardsImages.put(cardName,imageView);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }


    public BorderPane createLeftBox() throws IOException {
        BorderPane leftBox = new BorderPane();
        leftBox.setPrefWidth(bounds.getWidth() * 0.2);
        initOpponent3CardsLayout();
        leftBox.setCenter(opponent3CardsLayout);
        return leftBox;
    }

    public BorderPane createMiddleBox() throws IOException {
        BorderPane midBox = new BorderPane();
        midBox.setPrefWidth(bounds.getWidth() * 0.6);

        // data of each layout
        Button button = new Button("click");
        Button button1 = new Button("click1");
        Button button2 = new Button("click2");
        Button button3 = new Button("click3");

        // create bottom layout of cards
        initPlayerCardsLayout();
        midBox.setBottom(playerCardsLayout);

        // create top layout of cards
        VBox vBox = initOpponent2CardsLayout();
        midBox.setTop(vBox);

        // create middle layout of deck and pile
        midBox.setCenter(button1);

        return midBox;
    }

    public BorderPane createRightBox() throws IOException {
        BorderPane rightBox = new BorderPane();
        rightBox.setPrefWidth(bounds.getWidth() * 0.2);
        initOpponent1CardsLayout();
        rightBox.setCenter(opponent1CardsLayout);
        return rightBox;
    }

    public void initPlayerCardsLayout (){
        for (String key:playerCardsImages.keySet()) {
            ImageView imageView = playerCardsImages.get(key);
            playerCardsLayout.getChildren().add(imageView);
        }
        playerCardsLayout.setAlignment(Pos.CENTER);
    }
    public void initOpponent3CardsLayout() throws IOException {
        Opponent opponent = null;
        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==3){
                opponent = opponents.get(key);
            }
        }
        for (int i = 0; i < opponent.getNrCards(); i++) {
            opponent3CardsLayout.getChildren().add(getImage("Deck_reverse",Type.VERTICAL));
        }
        opponent3CardsLayout.setAlignment(Pos.CENTER_LEFT);
    }
    public VBox initOpponent2CardsLayout() throws IOException {
        VBox vBox = new VBox(10);
        Opponent opponent = null;
        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==2){
                opponent = opponents.get(key);
            }
        }
        for (int i = 0; i < opponent.getNrCards(); i++) {
            opponent2CardsLayout.getChildren().add(getImage("Deck",Type.HORIZONTAL));
        }
        opponent2CardsLayout.setAlignment(Pos.TOP_CENTER);
        Text name = new Text();
        name.setText(opponent.getName());
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(bounds.getWidth()*0.5);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(name);
        vBox.getChildren().addAll(opponent2CardsLayout,borderPane);
        return vBox;
    }
    public void initOpponent1CardsLayout() throws IOException {
        Opponent opponent = null;
        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==1){
                opponent = opponents.get(key);
            }
        }
        for (int i = 0; i < opponent.getNrCards(); i++) {
            //add player name

            //add player cards
            opponent1CardsLayout.getChildren().add(getImage("Deck_reverse",Type.VERTICAL));
        }
        opponent1CardsLayout.setAlignment(Pos.CENTER_RIGHT);
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
            imageView.setFitWidth(bounds.getWidth() * 0.07);
            imageView.setFitHeight(bounds.getWidth() * 0.05);
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

@Data
@AllArgsConstructor
class Opponent{
    private String name;
    private int position;
    private int nrCards;
}
