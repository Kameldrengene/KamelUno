package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Model.BootstrapButton;
import com.google.gson.Gson;
import com.kamelboyz.kameluno.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import lombok.Data;
import lombok.SneakyThrows;
import org.jspace.RemoteSpace;

import java.io.*;
import java.util.*;

@Data
public class GameBoard {

    private final Pane pane = new Pane();
    private Screen screen = Screen.getPrimary();
    private Rectangle2D bounds = screen.getVisualBounds();
    private HBox unoBtns = new HBox();

    private Group playerCardsLayout = new Group();
    private Group opponent1CardsLayout = new Group();
    private Group opponent2CardsLayout = new Group();
    private Group opponent3CardsLayout = new Group();
    private Group deck = new Group();
    private VBox middlelayout = new VBox(50);
    private BorderPane midBorder = new BorderPane();
    private StackPane pile = new StackPane();
    private Text turnText = new Text();
    private ArrayList<Pane> panes = new ArrayList<>();

    private Map<String, Opponent> opponents;
    private int lobbyId;
    private ChatView chatView;
    private RemoteSpace gameSpace;
    private Actions lastPlayed;
    private Button endTurnBtn = BootstrapButton.makeBootstrapButton("End Turn!", "btn-info");

    public GameBoard(Map<String, Opponent> opponents, int lobbyId, ChatView chatView, RemoteSpace gameSpace,Actions lastPlayed) {
        this.opponents = opponents;
        this.lobbyId = lobbyId;
        this.chatView = chatView;
        this.gameSpace = gameSpace;
        this.lastPlayed = lastPlayed;
        setRootLayout();
        initLayouts();
        onDeckClick();
    }

    public void setPaneDisable(boolean b){
        for (Pane p : panes){
            p.setDisable(b);
        }
    }

    public void setRootLayout(){
        pane.setPrefSize(bounds.getWidth(), bounds. getHeight()+20);
        InputStream stream = null;
        try {
            stream = getClass().getResource("/images/Table_1.png").openStream();
        } catch (IOException e) {

        }
        Image image = new Image(stream);
        pane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(bounds.getWidth(),bounds.getHeight()+50,false,false,false,false))));
    }

    public void initLayouts() {

        // root layout
        HBox rootLayout = new HBox(5);
        rootLayout.setPrefSize(bounds.getWidth(), bounds.getHeight());
        rootLayout.setPadding(new Insets(5, 5, 5, 5));

        //create left, middle and right vertical boxes
        try {
            Pane lb = createLeftBox();
            Pane mb = createMiddleBox();
            Pane rb = createRightBox();
            rootLayout.getChildren().addAll(lb,mb,rb);
            panes.add(mb);
            panes.add(rb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.getChildren().addAll(rootLayout);


    }


    public BorderPane createLeftBox() throws IOException {
        BorderPane leftBox = new BorderPane();
        leftBox.setPrefWidth(bounds.getWidth() * 0.2);
        HBox hBox = initOpponent3CardsLayout();
        panes.add(hBox);
        leftBox.setCenter(hBox);
        leftBox.setBottom(chatView.getChatWindow());
        return leftBox;
    }

    public BorderPane createMiddleBox() throws IOException {
        midBorder.setPrefWidth(bounds.getWidth() * 0.6);

        // create bottom layout of cards
        VBox vBoxPlayer = initPlayerCardsLayout(midBorder);
        midBorder.setBottom(vBoxPlayer);

        // create top layout of cards
        VBox vBoxOpponent2 = initOpponent2CardsLayout();
        midBorder.setTop(vBoxOpponent2);

        // create middle layout of deck and pile
        HBox hBox = new HBox(30);

        //deck layout
        double x = deck.getLayoutX();
        double y = deck.getLayoutY();
        System.out.println("X: " +x+ " y: "+y);

        for (int i = 0; i < 4; i++) {
            ImageView imageView1 = getImage("Deck",Type.HORIZONTAL);
            imageView1.setX(x+(i*5));
            imageView1.setY(y+(i*5));
            deck.getChildren().add(imageView1);
        }

        hBox.getChildren().addAll(pile,deck);
        hBox.setAlignment(Pos.CENTER);


        middlelayout.getChildren().addAll(hBox,turnText);
        middlelayout.setAlignment(Pos.CENTER);

        midBorder.setCenter(middlelayout);


        return midBorder;
    }

    public BorderPane createRightBox() throws IOException {
        BorderPane rightBox = new BorderPane();
        rightBox.setPrefWidth(bounds.getWidth() * 0.2);
        HBox hBox = initOpponent1CardsLayout();
        rightBox.setCenter(hBox);
        return rightBox;
    }
    //TODO: Make buttons do stuff
    private Button unoBtn = BootstrapButton.makeBootstrapButton("UNO!","btn-success");
    private Button noUnoBtn = BootstrapButton.makeBootstrapButton("Forgot UNO!","btn-danger");

    public VBox initPlayerCardsLayout (BorderPane borderPaneP) throws IOException {
        VBox vBox = new VBox(10);
        Text name = new Text();
        name.setText(Player.getInstance().getName());
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(bounds.getWidth()*0.5);
        BorderPane borderPane = new BorderPane();
        BorderPane unoPane = new BorderPane();
        borderPane.setCenter(name);
        unoBtns.getChildren().add(unoBtn);
        unoBtns.getChildren().add(noUnoBtn);
        unoBtns.getChildren().add(endTurnBtn);
        setEndTurnDisable(true);
        unoBtns.setAlignment(Pos.CENTER);
        unoPane.setCenter(unoBtns);
        BorderPane cardsBorder = new BorderPane();
        cardsBorder.setCenter(playerCardsLayout);
        onEndTurn();
        onUnoClick();
        onUnoForgottenClick();
        vBox.getChildren().addAll(unoPane,borderPane,cardsBorder);
        return vBox;
    }

    private void onEndTurn() {
        endTurnBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                new Thread(()->{
                    try {
                        Thread.sleep(500);
                        gameSpace.put(Player.getInstance().getName(), "ended");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                setPaneDisable(true);
                setEndTurnDisable(true);
            }
        });
    }

    private void onUnoClick(){
        unoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                gameSpace.put(Player.getInstance().getName(), "UNO");
            }
        });
    }

    private void onUnoForgottenClick(){
        noUnoBtn.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent actionEvent) {
                gameSpace.put(Player.getInstance().getName(), "missingUNO");
            }
        });
    }

    public HBox initOpponent3CardsLayout() throws IOException {
        HBox hBox = new HBox(10);
        Opponent opponent = null;

        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==3){
                opponent = opponents.get(key);
            }
        }
        updateOpponentCardLayout(opponent);
        Text name = new Text();
        name.setText(opponent.getName());
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(bounds.getWidth()*0.5);
        name.setRotate(270);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(name);
        BorderPane borderCards = new BorderPane();
        borderCards.setCenter(opponent3CardsLayout);
        hBox.getChildren().addAll(borderCards,borderPane);
        return hBox;
    }
    public VBox initOpponent2CardsLayout() throws IOException {
        VBox vBox = new VBox(10);
        Opponent opponent = null;
        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==2){
                opponent = opponents.get(key);
            }
        }
        updateOpponentCardLayout(opponent);
        Text name = new Text();
        name.setText(opponent.getName());
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(bounds.getWidth()*0.5);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(name);
        BorderPane borderCards = new BorderPane();
        borderCards.setCenter(opponent2CardsLayout);
        vBox.getChildren().addAll(borderCards,borderPane);
        return vBox;
    }
    public HBox initOpponent1CardsLayout() throws IOException {
        HBox hBox = new HBox(10);
        Opponent opponent = null;
        for (String key: opponents.keySet()) {
            if(opponents.get(key).getPosition()==1){
                opponent = opponents.get(key);
            }
        }
        updateOpponentCardLayout(opponent);
        Text name = new Text();
        name.setText(opponent.getName());
        //Setting font to the text
        name.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 35));
        name.setFill(Color.DEEPSKYBLUE);
        name.setX(bounds.getWidth()*0.5);
        name.setRotate(270);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(name);
        hBox.getChildren().addAll(borderPane,opponent1CardsLayout);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        return hBox;
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

    // update current players cards UI by adding card
    public void addPlayerCard(String cardName) throws IOException {
        double xPosPlayerCards = playerCardsLayout.getLayoutX();
        System.out.println("layout pos "+xPosPlayerCards);
        ImageView imageView = getImage(cardName,Type.HORIZONTAL);
        //imageView.setX(xPosPlayerCards+((playerCardsLayout.getChildren().size()-2)*60));
        imageView.setUserData(cardName);
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            // send to server
            // if response is success
            System.out.println("played "+imageView.getUserData());
        });
        playerCardsLayout.getChildren().add(imageView);
        double xpos = playerCardsLayout.getLayoutX();
        for (int i = 0; i < playerCardsLayout.getChildren().size(); i++) {
            ImageView imageView1 = (ImageView) playerCardsLayout.getChildren().get(i);
            imageView1.setX(xpos+(i*60));
        }
    }


    // update current players cards UI by removing card
    public void removePlayerCard(String cardName){
        int index = -1;
        for (int i = 0; i < playerCardsLayout.getChildren().size(); i++) {
            if(cardName.equals(playerCardsLayout.getChildren().get(i).getUserData())){
                System.out.println("founded "+ cardName);
                index = i;
            }
        }
        if(index!=-1){
            playerCardsLayout.getChildren().remove(index);
            double xpos = playerCardsLayout.getLayoutX();
            for (int i = 0; i < playerCardsLayout.getChildren().size(); i++) {
                ImageView imageView = (ImageView) playerCardsLayout.getChildren().get(i);
                imageView.setX(xpos+(i*60));
            }
        }
    }



    public void onDeckClick(){
        deck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @SneakyThrows
            @Override
            public void handle(MouseEvent mouseEvent) {
                Gson gson = new Gson();
                gameSpace.put(
                        Player.getInstance().getName(),
                        "action",
                        gson.toJson(new Action(Actions.DRAW,new Card()))
                );
                lastPlayed = Actions.DRAW;
            }
        });
    }



    public void updatePile(String cardName) throws IOException {
        pile.getChildren().add(getImage(cardName,Type.HORIZONTAL));
    }

    public void updatePlayerCards(List<Card> playerCards) throws IOException {
        double xPosPlayerCards = playerCardsLayout.getLayoutX();
        int i = 0;
        playerCardsLayout.getChildren().clear();
        for (Card card:playerCards) {
            String cardName = card.getColor()+"_"+card.getValue();
            ImageView imageView = getImage(cardName,Type.HORIZONTAL);
            imageView.setX(xPosPlayerCards+(i*60));
            imageView.setUserData(card);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
                System.out.println("clicked ");
                System.out.println(imageView.getUserData());
                Card cardToPlay = (Card) imageView.getUserData();
                Gson gson = new Gson();
                try {
                    gameSpace.put(
                            Player.getInstance().getName(),
                            "action",
                            gson.toJson(new Action(Actions.PLAY, cardToPlay))
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            playerCardsLayout.getChildren().add(imageView);
            i++;
        }
    }

    public void updateOpponentCardLayout(Opponent opponent) throws IOException {
        if (opponent.getPosition()==1){
            opponent1CardsLayout.getChildren().clear();
            double ypos = opponent1CardsLayout.getLayoutY();
            for (int i = 0; i < opponent.getNrCards(); i++) {
                ImageView imageView = getImage("Deck_reverse", Type.VERTICAL);
                imageView.setY(ypos + (i * 60));
                opponent1CardsLayout.getChildren().add(imageView);
            }
        }else if(opponent.getPosition()==2){
            opponent2CardsLayout.getChildren().clear();
            double xPos = opponent2CardsLayout.getLayoutX();
            for (int i = 0; i < opponent.getNrCards(); i++) {
                ImageView imageView = getImage("Deck",Type.HORIZONTAL);
                imageView.setX(xPos+(i*60));
                opponent2CardsLayout.getChildren().add(imageView);
            }
        }else {
            opponent3CardsLayout.getChildren().clear();
            double yPos = opponent3CardsLayout.getLayoutY();
            for (int i = 0; i < opponent.getNrCards(); i++) {
                ImageView imageView = getImage("Deck_reverse",Type.VERTICAL);
                imageView.setY(yPos+(i*60));
                opponent3CardsLayout.getChildren().add(imageView);
            }
        }
    }

    public void setWinnerWindow() throws IOException {
        WinView winView = new WinView(bounds.getWidth(),bounds.getHeight());
        VBox vBox = winView.getVBox();
        midBorder.getChildren().remove(middlelayout);
        midBorder.setCenter(vBox);
        playerCardsLayout.getChildren().clear();
        opponent1CardsLayout.getChildren().clear();
        opponent2CardsLayout.getChildren().clear();
        opponent3CardsLayout.getChildren().clear();
        unoBtns.getChildren().clear();
    }

    public void setLoseWindow(String winnerName) throws IOException {
        LoseView loseView = new LoseView(winnerName,bounds.getWidth(),bounds.getHeight());
        VBox vBox = loseView.getVBox();
        midBorder.getChildren().remove(middlelayout);
        midBorder.setCenter(vBox);
        playerCardsLayout.getChildren().clear();
        opponent1CardsLayout.getChildren().clear();
        opponent2CardsLayout.getChildren().clear();
        opponent3CardsLayout.getChildren().clear();
        unoBtns.getChildren().clear();
    }
    public void setTurnText(String playerName){
        turnText.setText(playerName);
        //Setting font to the text
        turnText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        turnText.setFill(Color.GREENYELLOW);
        turnText.setX(bounds.getWidth()*0.5);
    }

    public void setEndTurnDisable(boolean b) {
        endTurnBtn.setDisable(b);
    }
}

enum Type{
    VERTICAL,
    HORIZONTAL
}

