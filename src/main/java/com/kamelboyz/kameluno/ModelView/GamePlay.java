package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Model.Card;
import com.kamelboyz.kameluno.Model.Chat;
import com.kamelboyz.kameluno.Model.Opponent;
import com.kamelboyz.kameluno.Model.Player;
import com.kamelboyz.kameluno.Settings.Settings;
import lombok.Data;
import lombok.Getter;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        initialCards();
        gameBoard = new GameBoard(playerCards,opponents,lobbyId, chatView);
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
    public void calculateOpponentPosition(){

        //temporarily adding players
//        players.add("mike");
//        players.add("volkan");
//        players.add("mark");
//        players.add("talha");

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



    public void initialCards(){
        // temporarily adding cards
        playerCards.add(new Card("Red", "1"));
        playerCards.add(new Card("Blue", "1"));
        playerCards.add(new Card("Green", "1"));
        playerCards.add(new Card("Red", "2"));
        playerCards.add(new Card("Blue", "2"));
        playerCards.add(new Card("Green", "2"));
        playerCards.add(new Card("Yellow", "3"));
        playerCards.add(new Card("Blue", "3"));
        playerCards.add(new Card("Red", "3"));
        playerCards.add(new Card("Green", "3"));
        playerCards.add(new Card("Black", "Draw"));
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


