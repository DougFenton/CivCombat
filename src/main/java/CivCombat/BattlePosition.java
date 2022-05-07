package CivCombat;

import CivCombat.Unit.Unit;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.empty;

/**
 * A position in a battle. May contain a unit for each player.
 */
public final class BattlePosition {

  private Optional<Unit> attackerUnit;
  private Optional<Unit> defenderUnit;

  public BattlePosition(Optional<Unit> attackerUnit, Optional<Unit> defenderUnit) {
    this.attackerUnit = attackerUnit;
    this.defenderUnit = defenderUnit;
  }

  public BattlePosition() {
    attackerUnit = empty();
    defenderUnit = empty();
  }

  public BattlePosition(BattlePosition battlePosition) {
    this.attackerUnit = battlePosition.getAttackerUnit().map(Unit::copy);
    this.defenderUnit = battlePosition.getDefenderUnit().map(Unit::copy);
  }

  public BattlePosition copy() {
    return new BattlePosition(this);
  }

  public BattlePosition attackerUnit(Optional<Unit> unit) {
    attackerUnit = unit;
    return this;
  }

  public BattlePosition defenderUnit(Optional<Unit> unit) {
    defenderUnit = unit;
    return this;
  }

  public BattlePosition attackerUnit(Unit unit) {
    attackerUnit = Optional.of(unit);
    return this;
  }

  public BattlePosition defenderUnit(Unit unit) {
    defenderUnit = Optional.of(unit);
    return this;
  }

  public Optional<Unit> getAttackerUnit() {
    return attackerUnit;
  }

  public Optional<Unit> getDefenderUnit() {
    return defenderUnit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BattlePosition that = (BattlePosition) o;
    return Objects.equals(attackerUnit, that.attackerUnit) && Objects.equals(defenderUnit, that.defenderUnit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attackerUnit, defenderUnit);
  }

  @Override
  public String toString() {
    return "BattlePosition[" +
        "attackerUnit=" + attackerUnit + ", " +
        "defenderUnit=" + defenderUnit + ']';
  }

}
