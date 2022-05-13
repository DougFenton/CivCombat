/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CivCombat.Unit;

import java.util.Objects;

import static CivCombat.Unit.UnitType.AIRCRAFT;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Units have an attack value, a health value, a level, a type, and a number of wounds.
 * They can exist in a player's hand, or at a position on the battlefield.
 */
public abstract class Unit {
  private final int level;
  private final UnitType type;
  private final int attack;
  private final int health;

  // Once a unit's wounds reach its health, it is dead and it's wounds can no longer change.
  private int wounds;

  public Unit(UnitType type, int level, int attack, int health) {
    if (type == AIRCRAFT && level != 4) {
      throw new IllegalArgumentException("Aircraft must be level 4");
    }

    // Aircraft have a total of 2 more attack + health.
    int aircraftAdjustment = type == UnitType.AIRCRAFT ? 2 : 0;
    if (attack + health != level * 2 + 2 + aircraftAdjustment) {
      throw new InvalidUnitStatsException(type, level, attack, health);
    }
    this.level = level;
    this.type = type;
    this.attack = attack;
    this.health = health;
    this.wounds = 0;
  }

  public Unit(Unit original) {
    this.type = original.type;
    this.level = original.level;
    this.attack = original.attack;
    this.health = original.health;
    this.wounds = original.wounds;
  }

  public abstract Unit copy();

  public abstract boolean trumps(Unit unit);

  public abstract UnitType getUnitType();

  public int getLevel() {
    return level;
  }

  public int getAttack() {
    return attack;
  }

  public int getHealth() {
    return health;
  }

  public void applyWounds(int wounds) {
    if (isDead()) {
      throw new UnitIsDeadException();
    }
    this.wounds = min(this.wounds + wounds, this.getHealth());
  }

  public void removeWounds(int wounds) {
    if (isDead()) {
      throw new UnitIsDeadException();
    }
    this.wounds = max(this.wounds - wounds, 0);
  }

  public void removeWounds() {
    removeWounds(this.wounds);
  }

  public int getWounds() {
    return wounds;
  }

  public boolean isDead() {
    return wounds >= health;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Unit unit = (Unit) o;
    return level == unit.level && attack == unit.attack && health == unit.health && wounds == unit.wounds && type == unit.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, type, attack, health, wounds);
  }

  @Override
  public String toString() {
    return "Unit{" +
        "level=" + level +
        ", type=" + type +
        ", attack=" + attack +
        ", health=" + health +
        ", wounds=" + wounds +
        '}';
  }
}
