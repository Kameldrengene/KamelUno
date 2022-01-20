package com.kamelboyz.kameluno.ModelView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.kamelboyz.kameluno.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kamelboyz.kameluno.Settings.Settings;
import javafx.application.Platform;
import lombok.Getter;
import org.jspace.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public class GamePlay {


    // temp list to correctly place players
    private List<String> players = new ArrayList<>();

    // count each opponents' cards and position
    private Map<String, Opponent> opponents = new HashMap<>();

    private List<Card> playerCards = new ArrayList<>();
    private GameBoard gameBoard;
    private ChatView chatView;
    private int lobbyId;
    private RemoteSpace gameSpace;
    private Actions lastPlayed;
    private boolean gameDone;
    public GamePlay(int lobbyId, ChatView chatView) throws IOException, InterruptedException {
        this.lobbyId = lobbyId;
        this.chatView = chatView;
        gameSpace = new RemoteSpace("tcp://"+ Settings.getInstance().getServerIp() +"/game" + lobbyId + "?keep");
        getPlayers();
        createGameBoard();
    }
    private void getPlayers() throws InterruptedException {
        String[] playerIds = (String[]) gameSpace.get(
                new ActualField(Player.getInstance().getName()),
                new ActualField("players"),
                new FormalField(String[].class)
        )[2];

        for (int i = 0; i < playerIds.length; i++) {
            System.out.println("- " + playerIds[i]);
            players.add(playerIds[i]);
        }

    }

    private void createGameBoard() throws InterruptedException {
        calculateOpponentPosition();
        gameBoard = new GameBoard(opponents,lobbyId, chatView,gameSpace,lastPlayed);
        gameBoard.setPaneDisable(true);
        new Thread(new ClientHand(Player.getInstance().getName(),playerCards,gameSpace,gameBoard)).start();
        new Thread(new TakeTurn(gameSpace,gameBoard,gameDone)).start();
        new Thread(new TurnWatcher(Player.getInstance().getName(),gameSpace,gameBoard)).start();
        new Thread(new BoardUpdater(gameDone,gameSpace,Player.getInstance().getName(),gameBoard)).start();
        new Thread(new CheckUNO(Player.getInstance().getName(),gameSpace,gameDone,chatView)).start();
        new Thread(new CheckMissingUNO(Player.getInstance().getName(), gameSpace,gameDone,chatView)).start();


        TimeUnit.SECONDS.sleep(1);

        // Notify server the thread is ready
        gameSpace.put(Player.getInstance().getName(), "ready");

        // Wait until all players are ready
        gameSpace.get(
                new ActualField(Player.getInstance().getName()),
                new ActualField("allReady")
        );
        new Thread(new PerformAction(gameSpace,lastPlayed,gameBoard,Player.getInstance().getName())).start();




    }

    public boolean takeTurn() throws InterruptedException {
        // Wait for my turn
        String status = (String) gameSpace.get(
                new ActualField(Player.getInstance().getName()),
                new ActualField("take"),
                new FormalField(String.class)
        )[2];

        System.out.println("its my turn "+ Player.getInstance().getName());

        //Check if game is done
        if (!status.equals("alive")) {

            // update GUI to show winner or lose screen
            System.out.println("\nThe Winner is: " + status + "!");

            return false;
        }


        // Take turn
        gameSpace.put(Player.getInstance().getName(), "taken");

        Platform.runLater(()->{
            System.out.println("Your turn!");
            //gameBoard.getPane().setDisable(false);
        });

        //

        return true;
    }

    public void calculateOpponentPosition(){


        //find your position in array
        int myIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if(Player.getInstance().getName().equals(players.get(i))){
                myIndex = i;
            }
        }

        for (int i = 0; i < players.size() ; i++) {
            int index = (myIndex + i) % players.size();
            String name = players.get(index);
            System.out.println(name);
            // if it's an opponent
            if(i!=0){
                opponents.put(name,new Opponent(name,i,7));
            }
        }
    }





    public void updateBoard() throws IOException {
        //update top card
        gameBoard.updatePile("somecard");

        // update opponent cards
        for (String key:opponents.keySet()) {
            Opponent opponent = opponents.get(key);
            gameBoard.updateOpponentCardLayout(opponent);
        }

    }
}


class ClientHand implements Runnable{
    String playerId;
    RemoteSpace gameSpace;
    List<Card> playerCards;
    GameBoard gameBoard;

    public ClientHand(String playerId, List<Card> playerCards, RemoteSpace gameSpace, GameBoard gameBoard) {
        this.playerId = playerId;
        this.playerCards = playerCards;
        this.gameSpace = gameSpace;
        this.gameBoard = gameBoard;
    }


    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("before here");
                Object[] hand = gameSpace.get(
                        new ActualField(playerId),
                        new ActualField("cards"),
                        new FormalField(String.class)
                );
                System.out.println("cards received");
                System.out.println(hand[2]+"");
                ObjectMapper objectMapper = new ObjectMapper();
                Card[] cards = objectMapper.readValue(hand[2]+"",Card[].class);
                playerCards = Arrays.asList(cards);
                Platform.runLater(()->{
                    try {
                        gameBoard.updatePlayerCards(playerCards);
                        System.out.println("cards updated");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                System.out.println("im here");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class BoardWatcher{

}

class TurnWatcher implements Runnable{

    String playerId;
    RemoteSpace gameSpace;
    GameBoard gameBoard;

    public TurnWatcher(String playerId, RemoteSpace gameSpace,GameBoard gameBoard) {
        this.playerId = playerId;
        this.gameSpace = gameSpace;
        this.gameBoard = gameBoard;
    }
    @Override
    public void run() {
        try {
            while (true){
                String player = (String) gameSpace.get(
                        new ActualField(playerId),
                        new ActualField("takes"),
                        new FormalField(String.class)
                )[2];
                if(!player.equals(Player.getInstance().getName())){
                    //take turn
                    Platform.runLater(()->{
                        System.out.println(player+ " has the turn");
                        gameBoard.setTurnText(player+ " has the turn");
                    });
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class TakeTurn implements Runnable{
    RemoteSpace gameSpace;
    GameBoard gameBoard;
    boolean gameDone;
    public TakeTurn(RemoteSpace gameSpace,GameBoard gameBoard,boolean gameDone){
      this.gameSpace = gameSpace;
      this.gameBoard = gameBoard;
      this.gameDone = gameDone;
    }
    @Override
    public void run() {
        try {
            while (true){
                // Wait for my turn
                String status = (String) gameSpace.get(
                        new ActualField(Player.getInstance().getName()),
                        new ActualField("take"),
                        new FormalField(String.class)
                )[2];

                System.out.println("its my turn "+ Player.getInstance().getName());

                if(!status.equals("alive")) {
                    gameDone = true;
                    String winnerName = status;
                    if(winnerName.equals(Player.getInstance().getName())){
                        Platform.runLater(()->{
                            try {
                                gameBoard.setWinnerWindow();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }else {
                        Platform.runLater(()->{
                            try {
                                gameBoard.setLoseWindow(winnerName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    break;
                }

                // Take turn
                gameSpace.put(Player.getInstance().getName(), "taken","!");
                System.out.println(Player.getInstance().getName()+" has taken turn ");
                Platform.runLater(()->{
                    gameBoard.setTurnText("Your Turn");
                    gameBoard.setPaneDisable(false);
                    gameBoard.setEndTurnDisable(false);
                    System.out.println("board enabled for you");
                });
            }

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class PerformAction implements Runnable{

    RemoteSpace gameSpace;
    Actions lastPlayed;
    GameBoard gameBoard;
    String playerId;
    public PerformAction(RemoteSpace gameSpace,Actions lastPlayed, GameBoard gameBoard,String playerId){
        this.gameSpace = gameSpace;
        this.lastPlayed = lastPlayed;
        this.gameBoard = gameBoard;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        try {

            action: while (true){

        // Listen for response
            String response = (String) gameSpace.get(
                    new ActualField(playerId),
                    new FormalField(String.class)
            )[1];

            System.out.println("got response " + response);
            if(response.equals("success")){
//                gameSpace.put(playerId, "ended");
                System.out.println("got success response");


            } else if (response.equals("invalid")){
                System.out.println("invalid play, try again!");
            }

            break action;
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class BoardUpdater implements Runnable{

    boolean gameDone;
    RemoteSpace gameSpace;
    String playerId;
    GameBoard gameBoard;
    public BoardUpdater(boolean gameDone,RemoteSpace gameSpace,String playerId,GameBoard gameBoard){
        this.gameDone = gameDone;
        this.gameSpace = gameSpace;
        this.playerId = playerId;
        this.gameBoard = gameBoard;
    }
    @Override
    public void run() {
        try {
            while (!gameDone) {

                // Wait for signal
                String boardJson = (String) gameSpace.get(
                        new ActualField(playerId),
                        new ActualField("board"),
                        new FormalField(String.class)
                )[2];

                ObjectMapper objectMapper = new ObjectMapper();
                Board board = objectMapper.readValue(boardJson,Board.class);

                Platform.runLater(()->{
                    Card card = board.getTopCard();
                    String cardName = card.getColor()+"_"+card.getValue();
                    try {
                        gameBoard.updatePile(cardName);
                        System.out.println("pile updated");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Map<String,Integer> players = board.getHands();

                    for (String key:players.keySet()) {

                        // if it's an opponent
                        if(!key.equals(Player.getInstance().getName())){
                            Opponent opponent = gameBoard.getOpponents().get(key);
                            opponent.setNrCards(players.get(key));
                            try {
                                gameBoard.updateOpponentCardLayout(opponent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    System.out.println("game board updated");
                });
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CheckUNO implements Runnable{

    String playerId;
    RemoteSpace gameSpace;
    boolean gameDone;
    ChatView chatView;

    public CheckUNO(String playerId, RemoteSpace gameSpace,boolean gameDone,ChatView chatView) {
        this.playerId = playerId;
        this.gameSpace = gameSpace;
        this.chatView = chatView;
        this.gameDone = gameDone;
    }

    @Override
    public void run() {
        try {
            while (!gameDone) {

                // Wait for signal
                String caller = (String) gameSpace.get(
                        new ActualField(playerId),
                        new ActualField("UNO"),
                        new FormalField(String.class)
                )[2];
                Platform.setImplicitExit(false);
                Platform.runLater(()->{
                    chatView.getChatMessages().appendText(caller + ": UNO!\n");
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class CheckMissingUNO implements Runnable{

    String playerId;
    RemoteSpace gameSpace;
    boolean gameDone;
    ChatView chatView;

    public CheckMissingUNO(String playerId, RemoteSpace gameSpace,boolean gameDone,ChatView chatView) {
        this.playerId = playerId;
        this.gameSpace = gameSpace;
        this.gameDone = gameDone;
        this.chatView = chatView;
    }

    @Override
    public void run() {
        try {
            while (!gameDone) {

                // Wait for signal
                Object[] msg = gameSpace.get(
                        new ActualField(playerId),
                        new ActualField("UNO"),
                        new FormalField(String.class),
                        new FormalField(String.class)
                );

                String receiver = (String) msg[2];
                String caller = (String) msg[3];
                Platform.setImplicitExit(false);
                Platform.runLater(()->{
                    chatView.getChatMessages().appendText(caller + " called missing UNO on " + receiver +"\n");
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}