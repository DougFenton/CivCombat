/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat;

import CivCombat.Player.Player;
import CivCombat.Player.PlayerAction;
import CivCombat.Unit.Unit;
import CivCombat.Unit.UnitConsolePrinter;

import java.util.*;
import java.util.logging.Logger;

/**
 * Represents the units in hand and played for two players, attacker and defender.
 * At the start of a combat, the defender is the first to play a unit.
 */
public class Battlefield {
  private static final Logger LOGGER = Logger.getLogger(Battlefield.class.getName());

  //Determines number of positions to play units to
  private final int battlefieldSize;

  //Arrays containing units in play for each player, removed as units played
  private final Unit[] attackerUnits;
  private final Unit[] defenderUnits;

  //Lists of units not yet played
  private List<Unit> attackerHand;
  private List<Unit> defenderHand;

  public Battlefield(Player attacker, Player defender) {
    initializePlayerHands(attacker, defender);

    this.battlefieldSize = attackerHand.size() + defenderHand.size();

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
    this.attackerHand = new ArrayList<>(attacker.getUnitsList());
    this.defenderHand = new ArrayList<>(defender.getUnitsList());
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
      handSize = defenderHand.size();
    } else {
      options = this.getAttackerOptions();
      handSize =  attackerHand.size();
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

  public PlayerAction defaultAction(boolean defenderTurn) {
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
      newB.playDefenderUnit(action);
    } else {
      newB.playAttackerUnit(action);
    }
    return newB;
  }

  public Unit playAttackerUnit(PlayerAction action) {
    Unit unitPlayed;
    int handPosition = action.getHandPosition();
    int battlePosition = action.getBattlePosition();

    if (attackerUnits[battlePosition] != null) {
      throw new IllegalArgumentException("Attacker Position not empty");
    } else {
      if (attackerHand.size() - 1 < handPosition) {
        throw new IllegalArgumentException("Invalid Attacker Unit selected");
      } else {
        unitPlayed = attackerHand.remove(handPosition);
        LOGGER.info("Playing attacker unit at position " + battlePosition);
        LOGGER.info(UnitConsolePrinter.unitInfo(unitPlayed));

        attackerUnits[battlePosition] = unitPlayed;
        if (defenderUnits[battlePosition] != null) {
          fight(battlePosition);
        }
      }
    }
    return unitPlayed;
  }

  public Unit playDefenderUnit(PlayerAction action) {
    Unit unitPlayed;
    int handPosition = action.getHandPosition();
    int battlePosition = action.getBattlePosition();

    if (defenderUnits[battlePosition] != null) {
      throw new IllegalArgumentException("Defender Position not empty");
    } else {
      if (defenderHand.size() - 1 < handPosition) {
        throw new IllegalArgumentException("Invalid Defender Unit selected");
      } else {
        unitPlayed = defenderHand.remove(handPosition);
        LOGGER.info("Playing defender unit at position " + battlePosition);
        LOGGER.info(UnitConsolePrinter.unitInfo(unitPlayed));

        defenderUnits[battlePosition] = unitPlayed;
        if (attackerUnits[battlePosition] != null) {
          fight(battlePosition);
        }
      }
    }
    return unitPlayed;
  }

  public boolean allUnitsPlayed() {
    return (attackerHand.size() == 0 && defenderHand.size() == 0);
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
    return attackerStrength <= defenderStrength;
  }

  private void fight(int battlePosition) {
    LOGGER.info("Fight at position " + battlePosition);

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
      LOGGER.info("Attacker unit is slain in position " + battlePosition);
      LOGGER.info(UnitConsolePrinter.unitInfo(attacker));
      attacker.removeWounds();
      attackerUnits[battlePosition] = null;
    }
    if (defender.isDead()) {
      LOGGER.info("Defender unit is slain in position " + battlePosition);
      LOGGER.info(UnitConsolePrinter.unitInfo(defender));
      defender.removeWounds();
      defenderUnits[battlePosition] = null;
    }
  }

  public int getAttackerHandSize() {
    return attackerHand.size();
  }

  public int getDefenderHandSize() {
    return defenderHand.size();
  }

  @Override
  public String toString() {
    return "Battlefield {" + "attackerHand=" + attackerHand + ", defenderHand=" + defenderHand + ", battlefieldSize=" + battlefieldSize + ", attackerUnits=" + Arrays.toString(attackerUnits) + ", defenderUnits=" + Arrays.toString(defenderUnits) + '}';
  }
}
