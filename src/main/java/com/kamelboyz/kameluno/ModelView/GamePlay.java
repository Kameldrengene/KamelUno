package com.kamelboyz.kameluno.ModelView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kamelboyz.kameluno.Model.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamelboyz.kameluno.Model.Chat;
import com.kamelboyz.kameluno.Model.Opponent;
import com.kamelboyz.kameluno.Model.Player;
import com.kamelboyz.kameluno.Settings.Settings;
import javafx.application.Platform;
import lombok.Data;
import com.google.gson.Gson;
import lombok.Getter;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import java.io.IOException;
import java.util.*;

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

    private void createGameBoard(){
        calculateOpponentPosition();
        gameBoard = new GameBoard(opponents,lobbyId, chatView);
        new Thread(new ClientHand(Player.getInstance().getName(),playerCards,gameSpace,gameBoard)).start();
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

class TurnWatcher{

}