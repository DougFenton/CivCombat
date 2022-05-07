/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Player.PlayerAction;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Calculates the result of a combat using the Minimax algorithm.
 */
public class MinimaxCombat {

  protected Battlefield battlefield;

  public MinimaxCombat(Battlefield battlefield) {
    this.battlefield = battlefield;
  }

  /**
   * Returns the utility value of the game state, given that it is the defender's turn, and an action.
   * The action will be a winning action if there is one, or any action if there isn't.
   * If no actions are available, the action will be null.
   *
   * @return false for an attacker win, true for a defender win.
   */
  protected ActionAndResult maxValue(Battlefield battlefield) {
    //If battle is finished, calculate result.
    if (battlefield.allUnitsPlayed()) {
      return new ActionAndResult(null, battlefield.determineWinner());
    }

    //If defender cannot play, move to attacker turn
    if (battlefield.getDefenderOptions().isEmpty()) {
      return minValue(battlefield);
    }

    //Calculate if there is a winning play
    boolean value = false;
    Optional<PlayerAction> winningAction = Optional.empty();

    //For each move we can take
    for (PlayerAction action : battlefield.getDefenderOptions()) {
      //If we haven't already found a winning move
      if (!value) {
        //Check if playing a unit there wins
        ActionAndResult checkMove = minValue(battlefield.result(action));
        //If it does
        if (checkMove.result()) {
          value = true;
          //Remember it
          winningAction = Optional.of(action);
        }
      }
    }
    return new ActionAndResult(winningAction.orElse(battlefield.anyAction(true)), value);
  }

  /**
   * Returns the utility value of the game state, given that it is the attacker's turn, and an action.
   * The action will be a winning action if there is one, or any action if there isn't.
   * If no actions are available, the action will be null.
   *
   * @return false for an attacker win, true for a defender win.
   */
  protected ActionAndResult minValue(Battlefield battlefield) {
    //If battle is finished, calculate result.
    if (battlefield.allUnitsPlayed()) {
      return new ActionAndResult(null, battlefield.determineWinner());
    }

    //If attacker cannot play, move to defender turn
    if (battlefield.getAttackerOptions().isEmpty()) {
      return maxValue(battlefield);
    }

    //Calculate if there is a winning play
    boolean value = true;
    Optional<PlayerAction> winningAction = Optional.empty();

    //For each move we can take
    for (PlayerAction action : battlefield.getAttackerOptions()) {
      //If we haven't found a winning move
      if (value) {
        //Check if playing a unit there wins
        ActionAndResult checkMove = maxValue(battlefield.result(action));
        //If it does
        if (!checkMove.result()) {
          value = false;
          //Remember it
          winningAction = Optional.of(action);
        }
      }
    }

    return new ActionAndResult(winningAction.orElse(battlefield.anyAction(false)), value);
  }

  public ActionAndResult determineWinner(boolean defenderTurn) {
    return defenderTurn ? maxValue(battlefield) : minValue(battlefield);
  }

  /**
   * @return The set of actions which win with optimal play. Expensive to calculate.
   */
  public Set<PlayerAction> winningMoves(boolean defenderTurn) {
    Set<PlayerAction> moves = new LinkedHashSet<>();
    if (defenderTurn) {
      for (PlayerAction action : battlefield.getDefenderOptions()) {
        //minValue is false if attacker can win, true otherwise
        if (minValue(battlefield.result(action)).result()) {
          moves.add(action);
        }
      }
    } else {
      for (PlayerAction action : battlefield.getAttackerOptions()) {
        //maxValue is true if defender can win, false otherwise
        if (!maxValue(battlefield.result(action)).result()) {
          moves.add(action);
        }
      }
    }
    return moves;
  }
}
