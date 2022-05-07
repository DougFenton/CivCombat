/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Player.Player;
import CivCombat.Player.PlayerAction;
import CivCombat.Player.VillagePlayer;
import CivCombat.Possible.PossibleMinimaxCombat;
import CivCombat.Possible.PossiblePlayers;
import CivCombat.Possible.PossibleUnit;
import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import CivCombat.Unit.Unit;

import java.util.*;

/**
 * A console interface for simulating combats.
 */
public class ConsoleCombat {
  private Player attacker;
  private Player defender;
  private Battlefield battlefield;

  public ConsoleCombat() {
    initialize();
  }

  public static Player getSampleAttacker() {
    Set<Unit> units = new LinkedHashSet<>();
    units.add(new InfantryUnit(1, 2, 2));
    units.add(new MountedUnit(1, 2, 2));
    units.add(new ArtilleryUnit(1, 1, 3));
    return new Player(units);
  }

  public static Player getSampleDefender() {
    Set<Unit> units = new LinkedHashSet<>();
    units.add(new InfantryUnit(1, 1, 3));
    units.add(new MountedUnit(1, 2, 2));
    units.add(new ArtilleryUnit(1, 3, 1));
    return new Player(units);
  }

  public static void main(String[] args) {
    ConsoleCombat consoleCombat = new ConsoleCombat();
//    consoleCombat.playerCombat();
    consoleCombat.minmaxVillage();
    //consoleCombat.minimaxPlayer();
    //consoleCombat.testPossibleMinimax();
    //consoleCombat.possibleMinimaxPlayer();
  }

  private void initialize() {
    //Create attacker and defender
    attacker = new VillagePlayer();
    defender = new VillagePlayer();

    System.out.println("Printing attacker");
    attacker.printPlayer();

    System.out.println("Printing defender");
    defender.printPlayer();

    //Create battlefield
    battlefield = new Battlefield(attacker, defender);

    System.out.println("Initialised");
  }

  private void playerCombat() {
    //Repeat:
    //  Current player chooses unit in hand
    //  Current player chooses position on battlefield
    //  Play unit to chosen position
    //  Switch current player if opponent has units left
    //Until neither player has units left in hand
    //Determine winner of combat
    Scanner scanner = new Scanner(System.in);

    for (int i = 0; i < 3; i++) {
      // TODO: Improve this interface.
      System.out.println("Input hand position");
      int handPosition = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      int battlePosition = Integer.parseInt(scanner.nextLine());
      if (battlePosition == -1) {
        playDefenderUnit(handPosition);
      } else {
        playDefenderUnit(handPosition, battlePosition);
      }

      System.out.println("Input hand position");
      handPosition = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePosition = Integer.parseInt(scanner.nextLine());
      if (battlePosition == -1) {
        playAttackerUnit(handPosition);
      } else {
        playAttackerUnit(handPosition, battlePosition);
      }
    }

    if (battlefield.determineWinner()) {
      System.out.println("Defender wins");
    } else {
      System.out.println("Attacker wins");
    }
  }

  private void minmaxVillage() {
    MinimaxCombat minimax = new MinimaxCombat(battlefield);
    if (minimax.determineWinner(true).result()) {
      System.out.println("Defender wins");
    } else {
      System.out.println("Attacker wins");
    }
  }

  private void minimaxPlayer() {
    Scanner scanner = new Scanner(System.in);
    while (!battlefield.allUnitsPlayed()) {
      MinimaxCombat minimax = new MinimaxCombat(battlefield);
      ActionAndResult actionAndResult = minimax.determineWinner(true);
      boolean defenderWins = actionAndResult.result();
      PlayerAction action = actionAndResult.action();

      if (defenderWins) {
        System.out.println("\nI think Defender wins");
      } else {
        System.out.println("\nI think Attacker can win");
      }
      battlefield.playDefenderUnit(action);

      System.out.println("Input hand position");
      int handPosition = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      int battlePosition = Integer.parseInt(scanner.nextLine());
      playAttackerUnit(handPosition, battlePosition);
    }

    System.out.println();
    if (battlefield.determineWinner()) {
      System.out.println("Defender has won");
    } else {
      System.out.println("Attacker has won");
    }
  }

  private void testPossibleMinimax() {
    List<Unit> playedAttackerUnits = new ArrayList<>();
    List<Unit> playedDefenderUnits = new ArrayList<>();

    List<PossibleUnit> possibleAttackerUnits = new ArrayList<>();
    possibleAttackerUnits.add(new PossibleUnit("Infantry", 1));
    possibleAttackerUnits.add(new PossibleUnit("Mounted", 1));
    possibleAttackerUnits.add(new PossibleUnit("Artillery", 1));

    List<PossibleUnit> possibleDefenderUnits = new ArrayList<>();
    possibleDefenderUnits.add(new PossibleUnit("Infantry", 1));
    possibleDefenderUnits.add(new PossibleUnit("Mounted", 1));
    possibleDefenderUnits.add(new PossibleUnit("Artillery", 1));

    PossiblePlayers attackers = new PossiblePlayers(playedAttackerUnits, possibleAttackerUnits, 3);
    PossiblePlayers defenders = new PossiblePlayers(playedDefenderUnits, possibleDefenderUnits, 3);
    PossibleMinimaxCombat minimax = new PossibleMinimaxCombat(attacker, defenders, battlefield, true);
  }

  private void possibleMinimaxPlayer() {
    List<Unit> playedAttackerUnits = new ArrayList<>();

    List<PossibleUnit> possibleAttackerUnits = new ArrayList<>();
    possibleAttackerUnits.add(new PossibleUnit("Infantry", 1));
    possibleAttackerUnits.add(new PossibleUnit("Mounted", 1));
    possibleAttackerUnits.add(new PossibleUnit("Artillery", 1));

    Scanner scanner = new Scanner(System.in);
    int handPos;
    int battlePos;

    PossiblePlayers attackers;
    PossibleMinimaxCombat minimax;
    PlayerAction bestAction;
    Pair<Integer, Integer> actionEvaluation;

    while (!battlefield.allUnitsPlayed()) {
      attackers = new PossiblePlayers(playedAttackerUnits, possibleAttackerUnits, 3);
      minimax = new PossibleMinimaxCombat(defender, attackers, battlefield, true);
      bestAction = minimax.getBestAction();
      actionEvaluation = minimax.getActionEvaluation(bestAction);
      //System.out.println(battlefield.toString());

      System.out.println("I think the defender wins against " + actionEvaluation.getLeft() + " of " + actionEvaluation.getRight() + " possible opponents");
      battlefield.playDefenderUnit(bestAction);

      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      Unit playedUnit = playAttackerUnit(handPos, battlePos);
      playedAttackerUnits.add(playedUnit);
    }

    System.out.println();
    if (battlefield.determineWinner()) {
      System.out.println("Defender has won");
    } else {
      System.out.println("Attacker has won");
    }
  }

  private Unit playAttackerUnit(int handPosition, int battlePosition) {
    return battlefield.playAttackerUnit(new PlayerAction(battlefield.getAttackerUnits().get(handPosition), Optional.of(battlefield.getBattlePositions().get(battlePosition)), true));
  }

  private Unit playAttackerUnit(int handPosition) {
    return battlefield.playAttackerUnit(new PlayerAction(battlefield.getAttackerUnits().get(handPosition), Optional.empty(), true));
  }

  private Unit playDefenderUnit(int handPos, int battlePos) {
    return battlefield.playDefenderUnit(new PlayerAction(battlefield.getDefenderUnits().get(handPos), Optional.of(battlefield.getBattlePositions().get(battlePos)), false));
  }

  private Unit playDefenderUnit(int handPos) {
    return battlefield.playDefenderUnit(new PlayerAction(battlefield.getDefenderUnits().get(handPos), Optional.empty(), false));
  }

}
