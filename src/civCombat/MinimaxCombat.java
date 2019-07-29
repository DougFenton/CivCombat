/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civCombat;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Doug
 */
public class MinimaxCombat {

    protected Battlefield b;

    public MinimaxCombat(Battlefield b) {
        this.b = b;        
    }   

    //maxValue represents a defender turn
    protected Decision maxValue(Battlefield b) {
        /* function Max-Value(s) returns a utility value -
        false for attacker win, true for defender win */
        

        //If battle is finished, returns false for attacker win
        if (b.allUnitsPlayed()) {
            return new Decision(b.determineWinner(), null);
        }

        //If defender cannot play, move to attacker turn
        if (b.getDefenderHandSize() == 0) {
            return minValue(b);
        }

        //Calculate if there is a winning play
        boolean value = false;

        //Find default move in case no winning play available
        PlayerAction bestAction = b.defaultAction(true);

        //For each move we can take
        for (PlayerAction a : b.possibleActions(true)) {
            //If we haven't already found a winning move
            if (!value) {
                //Check if playing a unit there wins                
                Decision checkMove = minValue(b.result(true, a));
                //If it does                        
                if (checkMove.winner) {
                    value = true;
                    //Remember it                            
                    bestAction = a;
                }
            }
        }
        return new Decision(value, bestAction);
    }

    //minValue represents an attacker turn
    protected Decision minValue(Battlefield b) {
        /* function Min-Value(s) returns a utility value -
        false for attacker win, true for defender win   */

        //If battle is finished, returns false for attacker win
        if (b.allUnitsPlayed()) {
            return new Decision(b.determineWinner(), null);
        }

        //If attacker cannot play, move to defender turn
        if (b.getAttackerHandSize() == 0) {
            return maxValue(b);
        }

        //Calculate if there is a winning play
        boolean value = true;

        //Find default move in case no winning play available
        PlayerAction bestAction = b.defaultAction(false);

        //For each move we can take
        for (PlayerAction a : b.possibleActions(false)) {

            //If we haven't found a winning move
            if (value) {
                //Check if playing a unit there wins
                Decision checkMove = maxValue(b.result(false, a));
                //If it does
                if (!checkMove.winner) {
                    value = false;
                    //Remember it
                    bestAction = a;
                }
            }
        }

        return new Decision(value, bestAction);
    }

    public Decision determineWinner(boolean defenderTurn) {
        if (defenderTurn) {
            return maxValue(b);
        } else {
            return minValue(b);
        }
    }

    public Set<PlayerAction> winningMoves(boolean defenderTurn) {
        Set<PlayerAction> moves = new HashSet<>();
        if (defenderTurn) {
            for (PlayerAction a : b.possibleActions(defenderTurn)) {
                //minValue is false if attacker can win, true otherwise
                if (minValue(b.result(defenderTurn, a)).winner) {
                    moves.add(a);
                }
            }
        } else {
            for (PlayerAction a : b.possibleActions(defenderTurn)) {
                //maxValue is true if defender can win, false otherwise
                if (!maxValue(b.result(defenderTurn, a)).winner) {
                    moves.add(a);
                }
            }
        }
        return moves;
    }
}
