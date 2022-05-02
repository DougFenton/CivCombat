/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Possible;

import CivCombat.Battlefield;
import CivCombat.MinimaxCombat;
import CivCombat.Pair;
import CivCombat.Player.Player;
import CivCombat.Player.PlayerAction;

import java.util.*;
import java.util.Map.Entry;

/**
 * An extension of {@link MinimaxCombat}, that calculates all possible combats given no knowledge about the opponent's hand.
 */
public class PossibleMinimaxCombat {

  private final Player currentPlayer;
  private final PossiblePlayers possibleOpponents;
  private final Battlefield b;
  private final boolean defenderTurn;
  private final List<Set<PlayerAction>> winningActionsList;
  private final Map<PlayerAction, Integer> actionEvaluations;

  public PossibleMinimaxCombat(Player currentPlayer,
                               PossiblePlayers possibleOpponents, Battlefield b, boolean defenderTurn) {

    this.currentPlayer = currentPlayer;
    this.possibleOpponents = possibleOpponents;
    this.b = b;
    this.defenderTurn = defenderTurn;
    this.winningActionsList = generateWinningActions();
    this.actionEvaluations = actionEvaluations();
  }

  private List<Set<PlayerAction>> generateWinningActions() {
    List<Set<PlayerAction>> actionsList = new ArrayList<>();

    MinimaxCombat minimax;
    Battlefield bat;
    for (Player p : possibleOpponents.getPlayers()) {
      if (defenderTurn) {
        bat = new Battlefield(p, currentPlayer, b);
        minimax = new MinimaxCombat(bat);
      } else {
        bat = new Battlefield(currentPlayer, p, b);
        minimax = new MinimaxCombat(bat);
      }
      Set<PlayerAction> winningMoves = minimax.winningMoves(defenderTurn);
      actionsList.add(winningMoves);
    }
    return actionsList;
  }

  private Map<PlayerAction, Integer> actionEvaluations() {

    /* Counts the number of players from PossiblePlayers who lose to each possible action
     */
    Map<PlayerAction, Integer> evaluations = new HashMap<>();

    for (Set<PlayerAction> s : winningActionsList) {
      for (PlayerAction a : s) {
        if (evaluations.containsKey(a)) {
          evaluations.put(a, evaluations.get(a) + 1);
        } else {
          evaluations.put(a, 0);
        }
      }
    }
    return evaluations;

  }

  public PlayerAction getBestAction() {
    PlayerAction bestAction;

    bestAction = b.defaultAction(defenderTurn);
    int bestValue = 0;
    for (Entry<PlayerAction, Integer> e : actionEvaluations.entrySet()) {
      if (e.getValue() > bestValue) {
        bestValue = e.getValue();
        bestAction = e.getKey();
      }
    }
    return bestAction;
  }

  public Pair<Integer, Integer> getActionEvaluation(PlayerAction action) {
    int numberOfPossibleOpponents = possibleOpponents.getNumberOfPlayers();
    int actionEvaluation = actionEvaluations.getOrDefault(action, 0);
    return new Pair<>(actionEvaluation, numberOfPossibleOpponents);
  }
}
