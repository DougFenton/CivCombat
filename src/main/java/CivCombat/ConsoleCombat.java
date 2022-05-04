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

  private static Player attacker;
  private static Player defender;
  private static Battlefield battlefield;

  private static void initialize() {

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

  private Player getSampleAttacker() {
    Set<Unit> units = new LinkedHashSet<>();
    units.add(new InfantryUnit(1, 2, 2));
    units.add(new MountedUnit(1, 2, 2));
    units.add(new ArtilleryUnit(1, 1, 3));
    return new Player(units);
  }

  private Player getSampleDefender() {
    Set<Unit> units = new LinkedHashSet<>();
    units.add(new InfantryUnit(1, 1, 3));
    units.add(new MountedUnit(1, 2, 2));
    units.add(new ArtilleryUnit(1, 3, 1));
    return new Player(units);
  }

  private static void printAttackerOptions() {
    System.out.println("Printing open battlefield positions for attacker:");
    boolean[] options = battlefield.getAttackerOptions();
    for (int i = 0; i < options.length; i++) {
      if (options[i]) {
        System.out.print(i + " ");
      }
    }
  }

  private static void printDefenderOptions() {
    System.out.println("Printing open battlefield positions for defender:");
    boolean[] options = battlefield.getDefenderOptions();
    for (int i = 0; i < options.length; i++) {
      if (options[i]) {
        System.out.print(i + " ");
      }
    }
  }

  private static void testScript() {
    printAttackerOptions();

    battlefield.playDefenderUnit(new PlayerAction(0, 0));

    printAttackerOptions();

    battlefield.playAttackerUnit(new PlayerAction(1, 0));

    printAttackerOptions();
  }

  private static void playerCombat() {
    //Repeat:
    //  Current player chooses unit in hand
    //  Current player chooses position on battlefield
    //  Play unit to chosen position
    //  Switch current player if opponent has units left
    //Until neither player has units left in hand
    //Determine winner of combat
    int handPos;
    int battlePos;
    Scanner scanner = new Scanner(System.in);

    System.out.println("Printing attacker");
    attacker.printPlayer();

    System.out.println("Printing defender");
    defender.printPlayer();

    for (int i = 0; i < 3; i++) {

      printDefenderOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      battlefield.playDefenderUnit(new PlayerAction(handPos, battlePos));

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos));
    }

    if (battlefield.determineWinner()) {
      System.out.println("Defender wins");
    } else {
      System.out.println("Attacker wins");
    }
  }

  private static void minmaxVillage() {
    System.out.println("Printing attacker");
    attacker.printPlayer();

    System.out.println("Printing defender");
    defender.printPlayer();

    MinimaxCombat minimax = new MinimaxCombat(battlefield);
    if (minimax.determineWinner(true).result()) {
      System.out.println("Defender wins");
    } else {
      System.out.println("Attacker wins");
    }
  }

  private static void minimaxPlayer() {
    Scanner scanner = new Scanner(System.in);
    int handPos;
    int battlePos;

    MinimaxCombat minimax;
    Decision d;
    boolean winner;
    PlayerAction action;

    while (!battlefield.allUnitsPlayed()) {
      minimax = new MinimaxCombat(battlefield);
      d = minimax.determineWinner(true);
      winner = d.result();
      action = d.action();

      if (winner) {
        System.out.println("\nI think Defender wins");
      } else {
        System.out.println("\nI think Attacker can win");
      }
      battlefield.playDefenderUnit(action);

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos));
    }

    System.out.println();
    if (battlefield.determineWinner()) {
      System.out.println("Defender has won");
    } else {
      System.out.println("Attacker has won");
    }
  }

  private static void testPossibleMinimax() {
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

  private static void possibleMinimaxPlayer() {
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

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      Unit playedUnit = battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos));
      playedAttackerUnits.add(playedUnit);
    }

    System.out.println();
    if (battlefield.determineWinner()) {
      System.out.println("Defender has won");
    } else {
      System.out.println("Attacker has won");
    }
  }

  public static void main(String[] args) {
    initialize();
    testScript();
    //playerCombat();
    //minmaxVillage();
    //minimaxPlayer();
    //testPossibleMinimax();
    //possibleMinimaxPlayer();
  }

}
