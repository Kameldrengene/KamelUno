package com.kamelboyz.kameluno.ModelView;

import com.kamelboyz.kameluno.Model.Card;
import com.kamelboyz.kameluno.Model.Opponent;
import com.kamelboyz.kameluno.Model.Player;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GamePlay {

    // temp list to correctly place players
    private List<String> players = new ArrayList<>();

    // count each opponents' cards and position
    private Map<String, Opponent> opponents = new HashMap<>();

    private List<Card> playerCards = new ArrayList<>();

    private GameBoard gameBoard;

    public GamePlay(){
        createGameBoard();
    }

    private void createGameBoard(){
        calculateOpponentPosition();
        initialCards();
        gameBoard = new GameBoard(playerCards,opponents);
    }
    public void calculateOpponentPosition(){

        //temporarily adding players
        players.add("mike");
        players.add("volkan");
        players.add("mark");
        players.add("talha");

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

