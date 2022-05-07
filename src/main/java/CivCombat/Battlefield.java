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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Optional.empty;

/**
 * Represents the units in hand and played for two players, attacker and defender.
 * At the start of a combat, the defender is the first to play a unit.
 */
public class Battlefield {
  private static final Logger LOGGER = Logger.getLogger(Battlefield.class.getName());

  // The state of the units in play.
  private final List<BattlePosition> battlePositions;

  // The units not yet played.
  private List<Unit> attackerHand;
  private List<Unit> defenderHand;

  public Battlefield(Player attacker, Player defender) {
    initializePlayerHands(attacker, defender);
    battlePositions = new ArrayList<>();
  }

  /**
   * Creating a copy of battlefield, making new objects for every
   * reference type - i.e a deep copy
   */
  public Battlefield(Battlefield battlefield) {
    // Deep copy of attacker hand and defender hand.
    attackerHand = battlefield.attackerHand.stream().map(Unit::copy).collect(Collectors.toList());
    defenderHand = battlefield.defenderHand.stream().map(Unit::copy).collect(Collectors.toList());

    // Deep copy units in play.
    battlePositions = battlefield.battlePositions.stream().map(BattlePosition::copy).collect(Collectors.toList());
  }

  public Battlefield(Player attacker, Player defender, Battlefield currentBattlefield) {
    this(currentBattlefield);
    initializePlayerHands(attacker, defender);
  }

  private void initializePlayerHands(Player attacker, Player defender) {
    this.attackerHand = new ArrayList<>(attacker.getUnitsList());
    this.defenderHand = new ArrayList<>(defender.getUnitsList());
  }

  public Battlefield copy() {
    return new Battlefield(this);
  }

  public List<BattlePosition> getBattlePositions() {
    return battlePositions;
  }

  public List<Unit> getAttackerUnits() {
    return attackerHand;
  }

  public List<Unit> getDefenderUnits() {
    return defenderHand;
  }

  /**
   * @return All possible {@link PlayerAction}s the attacker can take.
   */
  public List<PlayerAction> getAttackerOptions() {
    List<PlayerAction> options = new ArrayList<>();

    for (Unit unit : attackerHand) {
      // Can attack a defender unit if friendly space is empty.
      for (BattlePosition battlePosition : battlePositions) {
        if (battlePosition.getAttackerUnit().isEmpty()) {
          options.add(new PlayerAction(unit, Optional.of(battlePosition), true));
        }
      }

      // Can open a new flank.
      options.add(new PlayerAction(unit, empty(), true));
    }

    return options;
  }

  /**
   * @return All possible {@link PlayerAction}s the defender can take.
   */
  public List<PlayerAction> getDefenderOptions() {
    List<PlayerAction> options = new ArrayList<>();

    for (Unit unit : defenderHand) {
      // Can attack an attacker unit if friendly space is empty.
      for (BattlePosition battlePosition : battlePositions) {
        if (battlePosition.getDefenderUnit().isEmpty()) {
          options.add(new PlayerAction(unit, Optional.of(battlePosition), false));
        }
      }

      // Can open a new flank.
      options.add(new PlayerAction(unit, empty(), false));
    }

    return options;
  }

  public PlayerAction anyAction(boolean defenderTurn) {
    List<PlayerAction> options = defenderTurn ? getDefenderOptions() : getAttackerOptions();
    return options.get(0);
  }

  protected Battlefield result(PlayerAction action) {
    // Create a new battlefield to return
    LOGGER.info("Copying battlefield");
    Battlefield newBattlefield = this.copy();

    // Update the action to target the battle position on the new battlefield
    PlayerAction newAction = updateActionForBattlefield(action, newBattlefield);

    // Play the desired unit
    if (action.isAttacker()) {
      newBattlefield.playAttackerUnit(newAction);
    } else {
      newBattlefield.playDefenderUnit(newAction);
    }
    LOGGER.info("newBattlefield = " + newBattlefield.prettyPrint());
    return newBattlefield;
  }

  /**
   * Makes a new PlayerAction corresponding to the previous, but on a new battlefield.
   */
  private PlayerAction updateActionForBattlefield(PlayerAction action, Battlefield newBattlefield) {
    List<Unit> units = action.isAttacker() ? newBattlefield.getAttackerUnits() : newBattlefield.getDefenderUnits();
    Unit newUnit = units.stream()
        .filter(unit -> action.unitPlayed().equals(unit))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No matching unit on new battlefield"));
    Optional<BattlePosition> newPosition = action.battlePosition()
        .map(battlePosition -> newBattlefield.battlePositions.stream()
            .filter(position -> position.equals(battlePosition))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No matching action on new battlefield.")));
    return new PlayerAction(newUnit, newPosition, action.isAttacker());
  }

  public Unit playAttackerUnit(PlayerAction action) {
    LOGGER.info("Playing attacker unit " + action.unitPlayed() + " at position " + action.battlePosition());
    if (!attackerHand.remove(action.unitPlayed())) {
      throw new IllegalStateException("Unit played does not match unit in attacker hand.");
    }
    if (action.battlePosition().isEmpty()) {
      battlePositions.add(new BattlePosition().attackerUnit(action.unitPlayed()));
    } else {
      BattlePosition battlePosition = action.battlePosition().get();
      if (battlePosition.getAttackerUnit().isPresent()) {
        throw new IllegalStateException("Attacker position not empty");
      }
      // Play unit.
      battlePosition.attackerUnit(action.unitPlayed());
      if (battlePosition.getDefenderUnit().isPresent()) {
        fight(battlePosition, this);
      }
    }
    return action.unitPlayed();
  }

  public Unit playDefenderUnit(PlayerAction action) {
    LOGGER.info("Playing defender unit " + action.unitPlayed() + " at position " + action.battlePosition());
    if (!defenderHand.remove(action.unitPlayed())) {
      throw new IllegalStateException("Unit played does not match unit in defender hand.");
    }
    if (action.battlePosition().isEmpty()) {
      battlePositions.add(new BattlePosition().defenderUnit(action.unitPlayed()));
    } else {
      BattlePosition battlePosition = action.battlePosition().get();
      if (battlePosition.getDefenderUnit().isPresent()) {
        throw new IllegalStateException("Defender position not empty");
      }
      // Play unit.
      battlePosition.defenderUnit(action.unitPlayed());
      if (battlePosition.getAttackerUnit().isPresent()) {
        fight(battlePosition, this);
      }
    }
    return action.unitPlayed();
  }

  public boolean allUnitsPlayed() {
    return (attackerHand.isEmpty() && defenderHand.isEmpty());
  }

  private void fight(BattlePosition battlePosition, Battlefield battlefield) {
    LOGGER.info("Fight at position " + battlePosition);

    Unit attacker = battlePosition.getAttackerUnit().orElseThrow(() -> new IllegalStateException("Cannot fight; no attacker unit."));
    Unit defender = battlePosition.getDefenderUnit().orElseThrow(() -> new IllegalStateException("Cannot fight; no defender unit."));

    // Apply wounds
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

    // Remove dead units
    if (attacker.isDead()) {
      LOGGER.info("Attacker unit is slain in position " + battlePosition);
      LOGGER.info(UnitConsolePrinter.unitInfo(attacker));
      battlePosition.attackerUnit(empty());
    }
    if (defender.isDead()) {
      LOGGER.info("Defender unit is slain in position " + battlePosition);
      LOGGER.info(UnitConsolePrinter.unitInfo(defender));
      battlePosition.defenderUnit(empty());
    }
    if (attacker.isDead() && defender.isDead()) {
      battlePositions.remove(battlePosition);
    }
    LOGGER.info("Resulting battlePosition: " + battlePosition);
  }

  /**
   * @return true if the defender wins, false if the attacker wins.
   */
  public boolean determineWinner() {
    int attackerStrength = 0;
    int defenderStrength = 0;
    for (BattlePosition battlePosition : battlePositions) {
      if (battlePosition.getAttackerUnit().isPresent()) {
        Unit attacker = battlePosition.getAttackerUnit().get();
        attackerStrength += attacker.getHealth() - attacker.getWounds();
      }

      if (battlePosition.getDefenderUnit().isPresent()) {
        Unit defender = battlePosition.getDefenderUnit().get();
        defenderStrength += defender.getHealth() - defender.getWounds();
      }
    }
    return attackerStrength <= defenderStrength;
  }

  @Override
  public String toString() {
    return "Battlefield{" +
        "battlePositions=" + battlePositions +
        ", attackerHand=" + attackerHand +
        ", defenderHand=" + defenderHand +
        '}';
  }

  public String prettyPrint() {
    return "Battlefield{" +
        "battlePositions=\n" +
        battlePositions.stream().map(BattlePosition::toString).map(s -> "    " + s + "\n").collect(Collectors.joining()) +
        "    , attackerHand=" + attackerHand + "\n" +
        "    , defenderHand=" + defenderHand +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Battlefield that = (Battlefield) o;
    return Objects.equals(battlePositions, that.battlePositions) && Objects.equals(attackerHand, that.attackerHand) && Objects.equals(defenderHand, that.defenderHand);
  }

  @Override
  public int hashCode() {
    return Objects.hash(battlePositions, attackerHand, defenderHand);
  }
}
