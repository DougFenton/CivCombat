/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Unit.ArtilleryUnit;
import CivCombat.Unit.InfantryUnit;
import CivCombat.Unit.MountedUnit;
import CivCombat.Unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Doug
 */
public class ConsoleCombat {

  private static Player attacker;
  private static Player defender;
  private static Battlefield battlefield;

  private static void initialize() {
    //Preset units
    Unit[] units1 = new Unit[3];
    units1[0] = new InfantryUnit(2, 2);
    units1[1] = new MountedUnit(2, 2);
    units1[2] = new ArtilleryUnit(1, 3);
    Player sampleAttacker = new Player(units1);

    Unit[] units2 = new Unit[3];
    units2[0] = new InfantryUnit(1, 3);
    units2[1] = new MountedUnit(2, 2);
    units2[2] = new ArtilleryUnit(3, 1);
    Player sampleDefender = new Player(units2);

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

  private static void printAttackerOptions() {
    System.out.println("Printing open battlefield positions for attacker:");
    boolean[] options = battlefield.getAttackerOptions();
    for (int i = 0; i < options.length; i++) {
      if (options[i]) {
        System.out.print(i + " ");
      }
    }

    System.out.println("\nAttacker has " + battlefield.getAttackerHandSize()
        + " units remaining");
    //System.out.println(Arrays.toString(options));
  }

  private static void printDefenderOptions() {
    System.out.println("Printing open battlefield positions for defender:");
    boolean[] options = battlefield.getDefenderOptions();
    for (int i = 0; i < options.length; i++) {
      if (options[i]) {
        System.out.print(i + " ");
      }
    }
    System.out.println("\nDefender has " + battlefield.getDefenderHandSize()
        + " units remaining");
    //System.out.println(Arrays.toString(options));
  }

  private static void testScript() {
    System.out.println("Printing attacker");
    attacker.printPlayer();

    System.out.println("Printing defender");
    defender.printPlayer();

    printAttackerOptions();

    battlefield.playDefenderUnit(new PlayerAction(0, 0), true);

    printAttackerOptions();

    battlefield.playAttackerUnit(new PlayerAction(1, 0), true);

    printAttackerOptions();
  }

  private static void testHandSize() {
    Battlefield b1 = battlefield.copyBattlefield();
    System.out.println(b1.getDefenderHandSize());
    battlefield.playDefenderUnit(new PlayerAction(0, 0), true);
    Battlefield b2 = battlefield.copyBattlefield();
    System.out.println(b1.getDefenderHandSize());
    System.out.println(b2.getDefenderHandSize());
  }

  private static void testUnits() {
    Unit[] units = new Unit[2];
    units[0] = new InfantryUnit(2, 2);
    units[1] = new MountedUnit(2, 2);
    System.out.println(units[0].trumps(units[1]));
    System.out.println(units[1].trumps(units[0]));
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
      battlefield.playDefenderUnit(new PlayerAction(handPos, battlePos), true);

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos), true);
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
    if (minimax.determineWinner(true).winner) {
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
      winner = d.winner;
      action = d.action;

      if (winner) {
        System.out.println("\nI think Defender wins");
      } else {
        System.out.println("\nI think Attacker can win");
      }
      battlefield.playDefenderUnit(action, true);

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos), true);
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
      battlefield.playDefenderUnit(bestAction, true);

      printAttackerOptions();
      System.out.println("Input hand position");
      handPos = Integer.parseInt(scanner.nextLine());
      System.out.println("Input battle position");
      battlePos = Integer.parseInt(scanner.nextLine());
      Unit playedUnit = battlefield.playAttackerUnit(new PlayerAction(handPos, battlePos), true);
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
    //testHandSize();
    //testUnits();
    //testScript();
    //playerCombat();
    //minmaxVillage();
    //minimaxPlayer();
    //testPossibleMinimax();
    possibleMinimaxPlayer();
  }

}
