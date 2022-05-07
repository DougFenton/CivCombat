/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Player;

import CivCombat.BattlePosition;
import CivCombat.Unit.Unit;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents playing a unit from hand to a given battle position, or to a new flank if `battlePosition` is empty.
 */
public final class PlayerAction {
  private final Unit unitPlayed;
  private final Optional<BattlePosition> battlePosition;
  private final boolean isAttacker;

  public PlayerAction(Unit unitPlayed, Optional<BattlePosition> battlePosition, boolean isAttacker) {
    this.unitPlayed = unitPlayed;
    this.battlePosition = battlePosition;
    this.isAttacker = isAttacker;
  }

  public Unit unitPlayed() {
    return unitPlayed;
  }

  public Optional<BattlePosition> battlePosition() {
    return battlePosition;
  }

  public boolean isAttacker() {
    return isAttacker;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (PlayerAction) obj;
    return Objects.equals(this.unitPlayed, that.unitPlayed) &&
        Objects.equals(this.battlePosition, that.battlePosition) &&
        this.isAttacker == that.isAttacker;
  }

  @Override
  public int hashCode() {
    return Objects.hash(unitPlayed, battlePosition, isAttacker);
  }

  @Override
  public String toString() {
    return "PlayerAction[" +
        "unitPlayed=" + unitPlayed + ", " +
        "battlePosition=" + battlePosition + ", " +
        "isAttacker=" + isAttacker + ']';
  }

}
