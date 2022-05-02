/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;


import CivCombat.Unit.Unit;

import java.util.*;

/**
 * Represents the units in hand and played for two players, attacker and defender.
 */
public class Battlefield {

  //Determines number of positions to play units to
  private final int battlefieldSize;
  //Arrays containing units in play for each player, removed as units played
  private final Unit[] attackerUnits;
  private final Unit[] defenderUnits;
  /**
   * At the start of a combat, the defender is the first to play a unit
   */
  //Lists of units not yet played
  private List<Unit> attackerHand;
  private List<Unit> defenderHand;
  private int attackerHandSize;
  private int defenderHandSize;

  public Battlefield(Player attacker, Player defender) {
    initializePlayerHands(attacker, defender);

    this.battlefieldSize = attackerHandSize + defenderHandSize;

    //No units played at start, no units killed
    this.attackerUnits = new Unit[battlefieldSize];
    this.defenderUnits = new Unit[battlefieldSize];
  }

  /**
   * Creating a copy of battlefield, making new objects for every
   * reference type - i.e a deep copy
   */
  public Battlefield(Battlefield b) {

    //Deep copy of attacker hand and defender hand
    List<Unit> newAttackerHand = new ArrayList<>();
    for (Unit u : b.attackerHand) {
      newAttackerHand.add(u.copyUnit());
    }
    this.attackerHand = newAttackerHand;

    List<Unit> newDefenderHand = new ArrayList<>();
    for (Unit u : b.defenderHand) {
      newDefenderHand.add(u.copyUnit());
    }
    this.defenderHand = newDefenderHand;

    //Sizes are ints, a primitive, so simply copy value
    this.attackerHandSize = b.attackerHandSize;
    this.defenderHandSize = b.defenderHandSize;
    this.battlefieldSize = b.battlefieldSize;

    //Deep copy attackerUnits and defenderUnits
    Unit[] newAttackerUnits = new Unit[battlefieldSize];
    for (int i = 0; i < battlefieldSize; i++) {
      if (b.attackerUnits[i] != null) {
        newAttackerUnits[i] = b.attackerUnits[i].copyUnit();
      }
    }
    this.attackerUnits = newAttackerUnits;

    Unit[] newDefenderUnits = new Unit[battlefieldSize];
    for (int i = 0; i < battlefieldSize; i++) {
      if (b.defenderUnits[i] != null) {
        newDefenderUnits[i] = b.defenderUnits[i].copyUnit();
      }
    }
    this.defenderUnits = newDefenderUnits;
  }

  public Battlefield(Player attacker, Player defender, Battlefield currentBattlefield) {
    this(currentBattlefield);
    initializePlayerHands(attacker, defender);
  }

  private void initializePlayerHands(Player attacker, Player defender) {
    this.attackerHand = attacker.getUnitsList();
    this.defenderHand = defender.getUnitsList();
    this.attackerHandSize = attacker.getNumberOfUnits();
    this.defenderHandSize = defender.getNumberOfUnits();
  }

  public Battlefield copyBattlefield() {
    return new Battlefield(this);
  }

  public boolean[] getAttackerOptions() {
    //Returns an array of possible positions to play at
    boolean[] options = new boolean[battlefieldSize];

    //Can attack a defending unit if friendly space is empty
    for (int i = 0; i < battlefieldSize; i++) {
      if (defenderUnits[i] != null && attackerUnits[i] == null) {
        options[i] = true;
      }
    }

    //Can play to first empty spot
    boolean emptySpace = false;
    for (int i = 0; i < battlefieldSize; i++) {
      if (!emptySpace && attackerUnits[i] == null
          && defenderUnits[i] == null) {
        options[i] = true;
        emptySpace = true;
      }
    }
    return options;
  }

  public boolean[] getDefenderOptions() {
    //Returns an array of possible positions to play at
    boolean[] options = new boolean[battlefieldSize];

    //Can attack an attacking unit if friendly space is empty
    for (int i = 0; i < battlefieldSize; i++) {
      if (attackerUnits[i] != null && defenderUnits[i] == null) {
        options[i] = true;
      }
    }

    //Can play to first empty spot
    boolean emptySpace = false;
    for (int i = 0; i < battlefieldSize; i++) {
      if (!emptySpace && attackerUnits[i] == null
          && defenderUnits[i] == null) {
        options[i] = true;
        emptySpace = true;
      }
    }
    return options;
  }

  protected Set<PlayerAction> possibleActions(boolean defenderTurn) {
    //Set of valid moves
    Set<PlayerAction> actions = new HashSet<>();
    //Find open positions on battlefield and number of units in hand
    boolean[] options;
    int handSize;
    if (defenderTurn) {
      options = this.getDefenderOptions();
      handSize = this.getDefenderHandSize();
    } else {
      options = this.getAttackerOptions();
      handSize = this.getAttackerHandSize();
    }
    //For each position on the battlefield
    for (int battlePosition = 0; battlePosition < options.length; battlePosition++) {
      //If we are allowed to play a unit there
      if (options[battlePosition]) {
        for (int handPosition = 0; handPosition < handSize; handPosition++) {
          //Add this to the set of possible actions
          actions.add(new PlayerAction(handPosition, battlePosition));
        }
      }
    }
    return actions;
  }

  PlayerAction defaultAction(boolean defenderTurn) {
    //Open positions on the battlefield
    boolean[] options;
    if (defenderTurn) {
      options = this.getDefenderOptions();
    } else {
      options = this.getAttackerOptions();
    }
    //First position we are allowed to play to
    int firstOption = 0;
    for (int i = options.length - 1; i >= 0; i--) {
      if (options[i]) {
        firstOption = i;
      }
    }
    return new PlayerAction(0, firstOption);
  }

  protected Battlefield result(boolean defenderPlayed, PlayerAction action) {
    //Create a new b to return
    Battlefield newB = this.copyBattlefield();
    //Play the desired unit
    if (defenderPlayed) {
      newB.playDefenderUnit(action, false);
    } else {
      newB.playAttackerUnit(action, false);
    }
    return newB;
  }

  public Unit playAttackerUnit(PlayerAction action, boolean log) {
    Unit unitPlayed;
    int handPosition = action.getHandPosition();
    int battlePosition = action.getBattlePosition();
    //System.out.println("Playing attacker unit " + handPosition + " at battle position " + battlePosition);

    if (attackerUnits[battlePosition] != null) {
      throw new IllegalArgumentException("Attacker Position not empty");
    } else {
      if (attackerHandSize - 1 < handPosition) {
        throw new IllegalArgumentException("Invalid Attacker Unit selected");
      } else {
        unitPlayed = attackerHand.remove(handPosition);
        this.attackerHandSize -= 1;
        if (log) {
          System.out.println("Playing attacker unit at position " + battlePosition);
          unitPlayed.printUnit();
          System.out.println();
        }

        attackerUnits[battlePosition] = unitPlayed;
        if (defenderUnits[battlePosition] != null) {
          fight(battlePosition, log);
        }
      }
    }
    return unitPlayed;
  }

  public Unit playDefenderUnit(PlayerAction action, boolean log) {
    Unit unitPlayed;
    int handPosition = action.getHandPosition();
    int battlePosition = action.getBattlePosition();

    if (defenderUnits[battlePosition] != null) {
      throw new IllegalArgumentException("Defender Position not empty");
    } else {
      if (defenderHandSize - 1 < handPosition) {
        throw new IllegalArgumentException("Invalid Defender Unit selected");
      } else {
        unitPlayed = defenderHand.remove(handPosition);
        this.defenderHandSize -= 1;
        if (log) {
          System.out.println("Playing defender unit at position " + battlePosition);
          unitPlayed.printUnit();
          System.out.println();
        }

        defenderUnits[battlePosition] = unitPlayed;
        if (attackerUnits[battlePosition] != null) {
          fight(battlePosition, log);
        }
      }
    }
    return unitPlayed;
  }

  public int getAttackerHandSize() {
    return attackerHandSize;
  }

  public int getDefenderHandSize() {
    return defenderHandSize;
  }

  public boolean allUnitsPlayed() {
    return (attackerHandSize == 0 && defenderHandSize == 0);
  }

  public boolean determineWinner() {
    //Returns false for attacker win, true for defender
    int attackerStrength = 0;
    int defenderStrength = 0;
    for (int i = 0; i < battlefieldSize; i++) {
      if (attackerUnits[i] != null) {
        attackerStrength += attackerUnits[i].getHealth()
            - attackerUnits[i].getWounds();
      }
      if (defenderUnits[i] != null) {
        defenderStrength += defenderUnits[i].getHealth()
            - defenderUnits[i].getWounds();
      }
    }
    //System.out.println("Attacker strength " + attackerStrength + " Defender Strength " + defenderStrength);
    return attackerStrength <= defenderStrength;
  }

  private void fight(int battlePosition, boolean log) {
    //System.out.println("Fight at position " + battlePosition);

    //Attacker is attacker's unit, regardless of which was played
    Unit attacker = attackerUnits[battlePosition];
    Unit defender = defenderUnits[battlePosition];

    //Apply wounds
    if (attacker.trumps(defender)) {
      defender.applyWounds(attacker.getAttack());
      if (!defender.isDead()) {
        attacker.applyWounds(defender.getAttack());
      }
    } else if (defender.trumps(attacker)) {
      attacker.applyWounds(defender.getAttack());
      if (!attacker.isDead()) {
        defender.applyWounds(attacker.getAttack());
      }
    } else {
      attacker.applyWounds(defender.getAttack());
      defender.applyWounds(attacker.getAttack());
    }

    //Heal and remove dead units
    if (attacker.isDead()) {
      if (log) {
        System.out.println("Attacker unit is slain in position " + battlePosition);
        attacker.printUnit();
        System.out.println();
      }
      attacker.removeWounds();
      attackerUnits[battlePosition] = null;
    }
    if (defender.isDead()) {
      if (log) {
        System.out.println("Defender unit is slain in position " + battlePosition);
        defender.printUnit();
        System.out.println();
      }
      defender.removeWounds();
      defenderUnits[battlePosition] = null;
    }
  }

  @Override
  public String toString() {
    return "Battlefield{" + "attackerHand=" + attackerHand + ", defenderHand=" + defenderHand + ", battlefieldSize=" + battlefieldSize + ", attackerUnits=" + Arrays.toString(attackerUnits) + ", defenderUnits=" + Arrays.toString(defenderUnits) + '}';
  }
}
